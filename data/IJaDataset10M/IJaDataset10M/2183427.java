package org.async.sql;

import org.async.core.Server;
import org.async.core.Static;
import org.async.core.Pipeline;
import org.async.net.NetDispatcher;
import org.simple.Bytes;
import org.protocols.JSON;
import org.async.sql.AnSQLite;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;

/**
 * An SQLite server that consumes and produces UTF-8 encoded JSON strings 
 * transported by netstrings (see <a 
 * href="http://laurentszyster.be/ansql"
 * >ansql</a>).
 * 
 * @h3 Synopsis
 * 
 * @p For test purposes only, do:
 * 
 * @pre java -cp lib/sqlite.jar;lib/asyncorg.jar 
 *   org.async.sql.AnSQLiteServer 
 * 
 * @p Listen for incoming TCP/IP connection at address 127.0.0.2 and port 
 * 3999, using the default connection timeout and maintenance precision 
 * intervals to open and close an in-memory database on demand.
 * 
 * @p For developpement purposes, specify a persistent storage:
 * 
 * @pre java -cp lib/sqlite.jar;lib/asyncorg.jar \
 *  org.async.sql.AnSQLiteServer <strong>storage.db</strong>
 * 
 * @p Note that <code>slqite_jni.dll</code> must be somewhere in your 
 * <code>java.library.path</code>. To extend that path, use the -D option:
 * 
 * @pre -Djava.library.path=./lib
 * 
 * @h3 Application
 * 
 * @p The purpose of AnSQLite is to provide each network database partition
 * its own server, file, memory and process while ensuring ACIDity of all
 * transactions. 
 * 
 * @p It suites well three general use cases of a relational database: to hold 
 * in memory a relatively small set of transient data; to protect a database 
 * partition from its online transaction controller failures; to support a 
 * large network of controllers; 
 * 
 * @p AnSQLite is mostly practical for web controllers that distribute
 * data presentation to applications running in the user agents. 
 * 
 */
public class AnSQLiteServer extends Server {

    protected static final class Channel extends NetDispatcher {

        protected AnSQLiteServer _server;

        private ByteBuffer _buffer;

        public Channel(AnSQLiteServer server) {
            super(server._loop, 16384, 16384);
            _server = server;
        }

        public final Object apply(Object value) throws Throwable {
            return null;
        }

        public final void handleConnect() throws Throwable {
            log("connected");
        }

        public final boolean handleLength(int length) throws Throwable {
            _buffer = ByteBuffer.wrap(new byte[length]);
            return true;
        }

        public final void handleData(byte[] data) throws Throwable {
            _buffer.put(data);
        }

        public final boolean handleTerminator() throws Throwable {
            try {
                StringBuilder response = new StringBuilder();
                _server.ansql.handle(_buffer.array(), response);
                push(Bytes.encode(response.toString(), Bytes.UTF8));
            } catch (Throwable e) {
                push(Bytes.encode(e.getMessage(), Bytes.UTF8));
            } finally {
                _buffer = null;
            }
            return false;
        }

        public final void handleClose() throws Throwable {
            log("close");
        }

        public final void close() {
            super.close();
            _server.serverClose(this);
            log("closed");
        }
    }

    public AnSQLite ansql;

    public final void serverWakeUp() {
        try {
            ansql.open();
        } catch (Exception e) {
            log(e);
            close();
            return;
        }
        super.serverWakeUp();
    }

    public final Pipeline serverAccept() {
        return new Channel(this);
    }

    public final void serverSleep() {
        try {
            ansql.close();
        } catch (Exception e) {
            log(e);
            return;
        }
        super.serverSleep();
    }

    public static final void main(String args[]) throws Throwable {
        Static.loop.hookShutdown();
        try {
            AnSQLiteServer server = new AnSQLiteServer();
            server.listen(new InetSocketAddress((args.length > 0) ? args[0] : "127.0.0.2", (args.length > 1) ? Integer.parseInt(args[1]) : 3999));
            server.ansql = new AnSQLite((args.length > 2) ? args[2] : ":memory:", (args.length > 3) ? Integer.parseInt(args[3]) : 0);
            if (args.length > 4) {
                server.timeout = Integer.parseInt(args[4]) * 1000;
            }
            if (args.length > 5) {
                server.precision = Integer.parseInt(args[5]) * 1000;
            }
            Static.loop.exits.add(server);
            server.log("listen " + JSON.dict(new Object[] { "database", server.ansql._path, "options", server.ansql._options }).toString());
        } catch (Throwable e) {
            Static.loop.log(e);
        }
        Static.loop.dispatch();
    }
}
