package de.buelowssiege.jaymail.threads;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import de.buelowssiege.jaymail.gui.InterfaceManager;
import de.buelowssiege.jaymail.gui.frames.ProgressFrame;

/**
 * This thread does the actual work of moving messages from one folder to
 * another.
 * 
 * @author Maximilian Schwerin
 * @version $Id: MessageMoveThread.java,v 1.1 2005/10/20 11:33:47 mschwerin Exp $
 */
public class MessageMoveThread extends ProgressThread {

    private Folder sourceFolder;

    private Folder destinationFolder;

    private Message[] messagesToMove;

    private String displayMessage;

    public MessageMoveThread(Folder sourceFolder, Folder destinationFolder, Message[] messagesToMove, ProgressFrame frame, String displayMessage) {
        super(frame);
        this.displayMessage = displayMessage;
        this.sourceFolder = sourceFolder;
        this.destinationFolder = destinationFolder;
        this.messagesToMove = messagesToMove;
    }

    public static void moveMessages(Folder sourceFolder, Folder destinationFolder, Message[] messagesToMove, ProgressFrame frame, String displayMessage) {
        if (messagesToMove.length > 0) {
            MessageMoveThread mmt = new MessageMoveThread(sourceFolder, destinationFolder, messagesToMove, frame, displayMessage);
            mmt.start();
        }
    }

    public void run() {
        try {
            setProgressBarMinMax(0, 2);
            setTitle(displayMessage + " 1 - " + messagesToMove.length);
            setProgressBar(1, displayMessage + " 1 - " + messagesToMove.length);
            sourceFolder.copyMessages(messagesToMove, destinationFolder);
            for (int i = 0; i < messagesToMove.length; i++) {
                messagesToMove[i].setFlag(Flags.Flag.DELETED, true);
            }
        } catch (Exception ex) {
            InterfaceManager.showError(ex);
        }
        disposeFrame();
    }
}
