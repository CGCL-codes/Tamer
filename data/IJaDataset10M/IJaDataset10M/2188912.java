package com.dukesoftware.viewlon3.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JOptionPane;
import com.dukesoftware.utils.data.QueueByRingBuffer;
import com.dukesoftware.utils.data.QueueOperator;
import com.dukesoftware.utils.thread.pattern.workerthread.ChannelWithThreadPool;
import com.dukesoftware.viewlon3.communicator.ViewlonCommunicator;
import com.dukesoftware.viewlon3.communicator.ViewlonCommunicatorWrapper;
import com.dukesoftware.viewlon3.data.common.DataControl;
import com.dukesoftware.viewlon3.data.relation.interfacetool.RelationManager;
import com.dukesoftware.viewlon3.data.relation.now.RelationManagerForViewlon;
import com.dukesoftware.viewlon3.gui.common.ViewlonDialog;

/**
 * Viewlonの全体をコントロールする最上位のクラスです。通信・GUIのすべての機能を持ちます。
 * 
 * 
 *
 *
 */
public class ViewlonMain {

    private static final int WORKER_NUM = 3;

    private static final int QUEUE_SIZE = 100;

    public static final String NAME = "Viewlon";

    public static final String VERSION = NAME + "　0.0β";

    public static final String INFO = "\nCopyright(C) 2009\nby Duke Software";

    private final ChannelWithThreadPool channel = new ChannelWithThreadPool(100, WORKER_NUM);

    private final ViewlonCommunicatorWrapper comController;

    private final DataControl d_con;

    private final RootFrame frame;

    private final QueueOperator<List<String>> queue = new QueueByRingBuffer<List<String>>(QUEUE_SIZE);

    private final ReceiveDataManager dataManager;

    private final RelationManager relationManager;

    private final int port;

    private final String ipaddr;

    public static final int DIVIDE_NUM = 16;

    public ViewlonMain(String ipaddr, int port, int snum, int rnum, int cnum) {
        this.port = port;
        this.ipaddr = ipaddr;
        d_con = new DataControl(snum, rnum, cnum);
        relationManager = new RelationManagerForViewlon(d_con);
        comController = new ViewlonCommunicatorWrapper(new ViewlonCommunicator(NAME, channel), d_con, relationManager);
        frame = new RootFrame(VERSION, d_con, relationManager, comController, this);
        dataManager = new ReceiveDataManager(channel, relationManager, d_con, queue);
    }

    public void initialize() {
        frame.initialize();
        frame.setVisible(true);
        channel.initialize();
        channel.startWorkers();
        d_con.start();
        dataManager.start();
        while (!comController.start(ipaddr, port, queue)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        comController.getInitializeData();
        comController.getRealObjectSize();
        comController.getObjectFrontBackSensor();
        comController.setFlag(true, true, true);
        comController.start();
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });
    }

    public void shutdown() {
        if (ViewlonDialog.exitDialog() == JOptionPane.YES_OPTION) {
            comController.shutdown();
            dataManager.shutdown();
            d_con.shutdown();
            frame.shutdown();
        }
    }
}
