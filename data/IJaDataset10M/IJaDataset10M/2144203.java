package jxsdgenerator.engine.util;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Class Console creates a Java Console for GUI based Java Applications. Once
 * created, a Console component receives all the data directed to the standard
 * output (System.out) and error (System.err) streams. 
 * <p>
 * For example, once a Java Console is created for an application, data passed
 * on to any methods of System.out (e.g., System.out.println(" ")) and
 * System.err (e.g., stack trace in case of uncought exceptions) will be
 * received by the Console.
 * <p>
 * Note that a Java Console can not be created for Applets to run on any
 * browsers due to security violations. Browsers will not let standard output
 * and error streams be redicted (for obvious reasons).
 *
 * @author Subrahmanyam Allamaraju (sallamar@cvimail.cv.com) 
 */
public class Console extends Frame implements StreamObserver {

    TextArea aTextArea;

    ObservableStream errorDevice;

    ObservableStream outputDevice;

    ByteArrayOutputStream _errorDevice;

    ByteArrayOutputStream _outputDevice;

    PrintStream errorStream;

    PrintStream outputStream;

    PrintStream _errorStream;

    PrintStream _outputStream;

    Button clear;

    Button close;

    /**
     * Creates a Java Console.
     */
    public Console() {
        super("Java Console");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        aTextArea = new TextArea();
        aTextArea.setEditable(false);
        aTextArea.setBackground(Color.BLACK);
        aTextArea.setForeground(Color.GREEN);
        clear = new Button("Clear");
        close = new Button("Close");
        clear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aTextArea.setText("");
            }
        });
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(1, 0));
        buttonPanel.add(clear);
        buttonPanel.add(close);
        setLayout(new BorderLayout());
        add("Center", aTextArea);
        add("South", buttonPanel);
        _errorStream = System.err;
        _outputStream = System.out;
        _outputDevice = new ByteArrayOutputStream();
        _errorDevice = new ByteArrayOutputStream();
        this.pack();
        this.setError();
        this.setOutput();
        enable();
    }

    /** 
     * Clears the Console. 
     */
    public void clear() {
        try {
            outputDevice.writeTo(_outputDevice);
        } catch (IOException e) {
        }
        outputDevice.reset();
        try {
            errorDevice.writeTo(_errorDevice);
        } catch (IOException e) {
        }
        errorDevice.reset();
        aTextArea.setText("");
    }

    /**
     * Sets the error device to the Console if not set already. 
     * @see #resetError
     */
    public final void setError() {
        errorDevice = new ObservableStream();
        errorDevice.addStreamObserver(this);
        errorStream = new PrintStream(errorDevice, true);
        System.setErr(errorStream);
    }

    /**
     * Resets the error device to the default. Console will no longer receive
     * data directed to the error stream.
     * @see #setError
     */
    public final void resetError() {
        System.setErr(_errorStream);
    }

    /**
     * Sets the output device to the Console if not set already.
     * @see #resetOutput
     */
    public final void setOutput() {
        outputDevice = new ObservableStream();
        outputDevice.addStreamObserver(this);
        outputStream = new PrintStream(outputDevice, true);
        System.setOut(outputStream);
    }

    /**
     * Resets the output device to the default. Console will no longer receive
     * data directed to the output stream.
     * @see #setOutput
     */
    public final void resetOutput() {
        System.setOut(_outputStream);
    }

    /**
     * Gets the minimumn size.
     */
    public Dimension getMinimumSize() {
        return new Dimension(800, 600);
    }

    /**
     * Gets the preferred size.
     */
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public void streamChanged() {
        aTextArea.append(outputDevice.toString());
        try {
            outputDevice.writeTo(_outputDevice);
        } catch (IOException e) {
        }
        outputDevice.reset();
        errorStream.checkError();
        aTextArea.append(errorDevice.toString());
        try {
            errorDevice.writeTo(_errorDevice);
        } catch (IOException e) {
        }
        errorDevice.reset();
    }

    /**
     * Returns contents of the error device directed to it so far. Calling 
     * <a href="#clear">clear</a> has no effect on the return data of this method.
     */
    public ByteArrayOutputStream getErrorContent() throws IOException {
        ByteArrayOutputStream newStream = new ByteArrayOutputStream();
        _errorDevice.writeTo(newStream);
        return newStream;
    }

    /**
     * Returns contents of the output device directed to it so far. Calling 
     * <a href="#clear">clear</a> has no effect on the return data of this method.
     */
    public ByteArrayOutputStream getOutputContent() throws IOException {
        ByteArrayOutputStream newStream = new ByteArrayOutputStream();
        _outputDevice.writeTo(newStream);
        return newStream;
    }
}
