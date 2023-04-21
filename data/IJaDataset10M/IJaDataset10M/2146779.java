package test;

import io.Repository;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import net.Protocol;
import net.ReliableDatagramReader;
import net.ReliableDatagramWriter;
import net.ReceiveVerifier;
import net.datagram.RDPacket;
import net.datagram.RDUtil;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

public class FileReceiver implements ReliableDatagramWriter.Listener, ReliableDatagramReader.Listener, ReceiveVerifier.Listener {

    private Shell sShell = null;

    private CLabel lblListenAt = null;

    private Text txtListenAt = null;

    private CLabel lblFileToReceive = null;

    private Text txtFileToReceive = null;

    private Button cmdBrowse = null;

    private Button cmdStart = null;

    private Repository rep;

    private DatagramSocket socket;

    private ReliableDatagramWriter writer;

    private ReliableDatagramReader reader;

    private RandomAccessFile raf;

    private int segmentsWritten;

    private ServerSocket ss;

    public FileReceiver() {
        try {
            ss = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rep = Repository.getInstance();
        rep.put(S.class);
        rep.put(B.class);
    }

    public boolean handleThrown(ReliableDatagramWriter writer, Throwable th) {
        return false;
    }

    public boolean timeout(ReliableDatagramWriter writer, RDPacket p) {
        return false;
    }

    public boolean handleThrown(ReliableDatagramReader reader, Throwable th) {
        return false;
    }

    public void received(ReliableDatagramReader reader, RDPacket packet) {
        try {
            if (!RDUtil.isAck(packet)) {
                if (reader.getReceiveVerifier().verify(packet)) {
                    System.out.println("RECEIVED");
                    segmentsWritten++;
                    int srcFrom = Protocol.R_CONTENT;
                    int srcLen = packet.getLength() - Protocol.R_CONTENT;
                    int fixSrcLen = Protocol.RELIABLE_DATAGRAM_MAX_LENGTH - Protocol.R_CONTENT;
                    int destFrom = fixSrcLen * RDUtil.getSegIndex(packet);
                    raf.seek(destFrom);
                    raf.write(packet.getData(), srcFrom, srcLen);
                    if (segmentsWritten == RDUtil.getSegCount(packet)) {
                        raf.close();
                        System.out.println("FINISHED");
                    }
                }
                writer.sendAck(packet);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean expire(RDPacket packet) {
        return false;
    }

    public void expired(int count) {
    }

    /**
	 * This method initializes sShell
	 */
    private void createSShell() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.verticalAlignment = GridData.CENTER;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalSpan = 2;
        gridData.verticalAlignment = GridData.CENTER;
        sShell = new Shell();
        sShell.setText("File Receiver");
        sShell.setLayout(gridLayout);
        sShell.setSize(new Point(300, 200));
        sShell.addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent e) {
                System.exit(0);
            }
        });
        lblListenAt = new CLabel(sShell, SWT.NONE);
        lblListenAt.setText("Listen at (host:port)");
        Label filler2 = new Label(sShell, SWT.NONE);
        txtListenAt = new Text(sShell, SWT.BORDER);
        txtListenAt.setText("localhost:8000");
        txtListenAt.setLayoutData(gridData);
        lblFileToReceive = new CLabel(sShell, SWT.NONE);
        lblFileToReceive.setText("Save file at");
        Label filler = new Label(sShell, SWT.NONE);
        txtFileToReceive = new Text(sShell, SWT.BORDER);
        txtFileToReceive.setLayoutData(gridData1);
        txtFileToReceive.setText("C:\\test.txt");
        cmdBrowse = new Button(sShell, SWT.NONE);
        cmdBrowse.setText("Browse");
        cmdBrowse.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                FileDialog d = new FileDialog(sShell, SWT.SAVE);
                d.setText("Browse");
                String[] filterExt = { "*.*" };
                d.setFilterExtensions(filterExt);
                String selected = d.open();
                txtFileToReceive.setText(selected);
            }
        });
        cmdStart = new Button(sShell, SWT.NONE);
        cmdStart.setText("Start Receiving!");
        cmdStart.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                try {
                    Socket s = ss.accept();
                    System.out.println("CLIENT ACCEPTED");
                    String[] address = txtListenAt.getText().split(":");
                    socket = new DatagramSocket(new InetSocketAddress(address[0], Integer.parseInt(address[1])));
                    writer = new ReliableDatagramWriter(socket, FileReceiver.this);
                    reader = new ReliableDatagramReader(socket, FileReceiver.this, FileReceiver.this);
                    File f = new File(txtFileToReceive.getText());
                    raf = new RandomAccessFile(f, "rw");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        cmdStart.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                System.out.println("widgetSelected()");
            }
        });
    }

    /**
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        Display display = Display.getDefault();
        FileReceiver thisClass = new FileReceiver();
        thisClass.createSShell();
        thisClass.sShell.open();
        while (!thisClass.sShell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
