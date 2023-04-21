package org.vfsutils.shell.remote;

import java.io.ByteArrayOutputStream;
import java.io.FilterReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintStream;
import java.io.Reader;
import org.vfsutils.shell.Engine;
import bsh.ConsoleInterface;

public class StreamRunner2 implements EngineRunner {

    protected class BlockingReader extends FilterReader {

        public BlockingReader(Reader reader) {
            super(reader);
        }

        public int read() throws IOException {
            if (stopRequested) {
                throw new IOException("read aborted");
            }
            if (!this.in.ready()) {
                out.flush();
                err.flush();
                System.out.println("read: " + outBuffer.toString());
                waitForOutput.release();
                waitForInput.acquire();
                if (stopRequested) {
                    throw new IOException("read aborted");
                }
            }
            int read = super.read();
            if (stopRequested) {
                throw new IOException("read aborted");
            }
            return read;
        }

        public int read(char[] cbuf, int off, int len) throws IOException {
            if (stopRequested) {
                throw new IOException("read aborted");
            }
            if (!this.in.ready()) {
                out.flush();
                err.flush();
                System.out.println("readbuf: " + outBuffer.toString());
                waitForOutput.release();
                waitForInput.acquire();
                if (stopRequested) {
                    throw new IOException("read aborted");
                }
            }
            int read = super.read(cbuf, off, len);
            if (stopRequested) {
                throw new IOException("read aborted");
            }
            return read;
        }
    }

    protected class EngineThread extends Thread {

        private Engine engine;

        public EngineThread(Engine engine) {
            this.engine = engine;
        }

        public void run() {
            try {
                engine.go();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void requestStop() {
            engine.stopOnNext();
        }
    }

    protected class Semaphore {

        private String name;

        private int count;

        public Semaphore(String name, int n) {
            this.name = name;
            this.count = n;
        }

        public synchronized void acquire() {
            while (count == 0) {
                try {
                    wait();
                    System.out.println("acquire " + name + " " + count + ": continue");
                } catch (InterruptedException e) {
                }
            }
            count--;
        }

        public synchronized void release() {
            count++;
            System.out.println("release " + name + " " + count);
            notify();
        }
    }

    protected Semaphore waitForInput = new Semaphore("wairForInput", 0);

    protected Semaphore waitForOutput = new Semaphore("waitForOutput", 0);

    protected boolean stopRequested = false;

    protected PipedWriter inBuffer = new PipedWriter();

    protected FilterReader reader;

    protected ByteArrayOutputStream outBuffer = new ByteArrayOutputStream(2048);

    protected PrintStream out = new PrintStream(outBuffer);

    protected ByteArrayOutputStream errBuffer = new ByteArrayOutputStream(2048);

    protected PrintStream err = new PrintStream(errBuffer);

    protected EngineThread engineThread = null;

    public StreamRunner2() throws IOException {
        PipedReader pipedReader = new PipedReader(inBuffer);
        this.reader = new BlockingReader(pipedReader);
    }

    public void startEngine(Engine engine) {
        engineThread = new EngineThread(engine);
        engineThread.start();
    }

    public ShellResponse stopEngine() {
        if (!engineThread.isAlive()) {
            return new ShellResponse("", "Engine has stopped");
        }
        ShellResponse result = new ShellResponse();
        try {
            outBuffer.reset();
            errBuffer.reset();
            engineThread.requestStop();
            stopRequested = true;
            waitForInput.release();
            inBuffer.close();
            engineThread.join(5000);
            errBuffer.flush();
            outBuffer.flush();
            result.setErr(errBuffer.toString());
            result.setOut(outBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ShellResponse handleInput(ShellRequest request) throws IOException {
        if (!engineThread.isAlive()) {
            return new ShellResponse("", "Engine has stopped");
        }
        String input = request.getIn();
        if (input == null) {
            return new ShellResponse("", "Invalid input");
        } else if (input.equals("exit") || input.equals("bye") || input.equals("quite")) {
            return stopEngine();
        }
        outBuffer.reset();
        errBuffer.reset();
        inBuffer.write(input.endsWith("\n") ? input : input + "\n");
        inBuffer.flush();
        waitForInput.release();
        waitForOutput.acquire();
        err.flush();
        out.flush();
        ShellResponse result = new ShellResponse(outBuffer.toString(), errBuffer.toString());
        return result;
    }

    public void error(Object arg0) {
        this.err.println(arg0);
    }

    public PrintStream getErr() {
        return this.err;
    }

    public Reader getIn() {
        return this.reader;
    }

    public PrintStream getOut() {
        return this.out;
    }

    public void print(Object arg0) {
        this.out.print(arg0);
    }

    public void println(Object arg0) {
        this.out.println(arg0);
    }

    public static void main(String[] args) throws Exception {
        StreamRunner2 runner = new StreamRunner2();
        Engine engine = new Engine(runner);
        runner.startEngine(engine);
        System.out.println(runner.handleInput(new ShellRequest("pwd")));
        System.out.println(runner.handleInput(new ShellRequest("ls")));
        System.out.println(runner.handleInput(new ShellRequest("cd ..")));
        System.out.println(runner.handleInput(new ShellRequest("cd d:/temp")));
        System.out.println(runner.handleInput(new ShellRequest("open -up zip://d:/temp/rules.zip")));
        System.out.println(runner.handleInput(new ShellRequest("usr")));
        System.out.println(runner.handleInput(new ShellRequest("pwd")));
        System.out.println(runner.handleInput(new ShellRequest("close")));
        System.out.println(runner.handleInput(new ShellRequest("exit")));
        System.out.println(runner.handleInput(new ShellRequest("close")));
        System.out.println(runner.stopEngine());
    }
}
