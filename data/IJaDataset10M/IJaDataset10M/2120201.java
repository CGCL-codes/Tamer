package org.robocup.msl.refbox.applications.teamSetupTool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import org.robocup.msl.refbox.ConfigFileReader;
import org.robocup.msl.refbox.CommunicationClient;
import org.robocup.msl.refbox.communication.ConnectionController;
import org.robocup.msl.refbox.communication.MultiCastSendConnectionHandler;
import org.robocup.msl.refbox.data.ConfigurationData;
import org.robocup.msl.refbox.protocol.Protocol2010;

public class TeamSetupTool implements Runnable {

    private static final int NTHREADS = 20;

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(NTHREADS);

    private UploadTeamDataFrame tca;

    private final ConnectionController connectionController = new ConnectionController();

    private int maxPlayersOnField;

    private int minPlayersOnField;

    private int minPlayersOnFieldAtStart;

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(final String[] args) {
        final TeamSetupTool rbm = new TeamSetupTool();
        rbm.start();
    }

    public UploadTeamDataFrame getTca() {
        return this.tca;
    }

    public void createStatusDisplayer(final ConnectionController cc) {
        this.tca = new UploadTeamDataFrame(cc, this.maxPlayersOnField, this.minPlayersOnField, this.minPlayersOnFieldAtStart);
        this.tca.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.tca.setVisible(true);
    }

    public void start() {
        final ConfigFileReader cfr = new ConfigFileReader();
        final ConfigurationData configData = cfr.read();
        this.maxPlayersOnField = configData.getMaxRobotPerTeamOnTheField();
        this.minPlayersOnField = configData.getMinRobotPerTeamOnTheField();
        this.minPlayersOnFieldAtStart = configData.getMinRobotPerTeamOnTheFieldAtStart();
        this.setMultiCastAddress(configData.getDataCommunication().iterator().next().getMulticastAddress());
        run();
    }

    private static final int PROTOCOL2010_OUTPORT = 30010;

    private String multiCastAddress;

    @Override
    public void run() {
        Runnable guiTask = new Runnable() {

            @Override
            public void run() {
                createStatusDisplayer(getConnectionController());
            }
        };
        Runnable sendTask = new Runnable() {

            @Override
            public void run() {
                try {
                    while ((TeamSetupTool.this.getTca() == null) || !getTca().isVisible()) {
                        Thread.yield();
                    }
                    final CommunicationClient gameControl = new CommunicationClient();
                    final MultiCastSendConnectionHandler handler2010 = new MultiCastSendConnectionHandler(new Protocol2010(gameControl), TeamSetupTool.PROTOCOL2010_OUTPORT, getMultiCastAddress());
                    getConnectionController().addHandler(handler2010);
                    getConnectionController().runHandlers(getExecutor());
                } catch (final Throwable t) {
                    t.printStackTrace();
                }
            }
        };
        getExecutor().execute(guiTask);
        getExecutor().execute(sendTask);
    }

    public void dispose() {
        if (this.tca != null) {
            this.tca.dispose();
        }
    }

    /**
     * @return the connectionController
     */
    public ConnectionController getConnectionController() {
        return this.connectionController;
    }

    public static Executor getExecutor() {
        return EXECUTOR;
    }

    public void setMultiCastAddress(final String multiCastAddress) {
        this.multiCastAddress = multiCastAddress;
    }

    public String getMultiCastAddress() {
        return this.multiCastAddress;
    }
}
