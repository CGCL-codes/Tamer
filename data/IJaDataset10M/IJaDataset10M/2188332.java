package pl.swmud.ns.swaedit.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import pl.swmud.ns.swaedit.core.JAXBOperations;
import pl.swmud.ns.swaedit.usmessages.Message;
import pl.swmud.ns.swaedit.usmessages.Messages;
import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QWidget;

public class MessagesWidget extends QWidget {

    class Model extends QAbstractListModel {

        public Object data(QModelIndex index, int role) {
            if (role == Qt.ItemDataRole.DisplayRole || role == Qt.ItemDataRole.ToolTipRole) {
                Message msg = messages.getMessage().get(index.row());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
                return msg.getTitle() + " (" + sdf.format(new Date(msg.getTimestamp().longValue() * 1000)) + ")";
            }
            return null;
        }

        public int rowCount(QModelIndex parent) {
            return messages.getMessage().size();
        }
    }

    private Ui_MessagesWidget ui = new Ui_MessagesWidget();

    private Messages messages;

    private boolean modified = false;

    public MessagesWidget() {
        ui.setupUi(this);
        SWAEdit.setChildPosition(this);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
        setWindowModality(Qt.WindowModality.ApplicationModal);
        messages = JAXBOperations.unmarshallMessages("data/usmessages.xml");
        ui.deleteButton.setEnabled(messages.getMessage().size() > 0);
        if (messages.getMessage().size() > 0) {
            ui.messagesView.setModel(new Model());
            fillMessage(0);
            ui.messagesView.clicked.connect(this, "fillMessage(QModelIndex)");
        }
    }

    protected void closeEvent(QCloseEvent e) {
        if (modified) {
            JAXBOperations.marshall(messages, "data/usmessages.xml", "schemas/usmessages.xsd");
        }
        super.closeEvent(e);
    }

    private void fillMessage(int idx) {
        Message msg = messages.getMessage().get(idx);
        ui.lAuthor.setText(msg.getAuthor());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        ui.lDate.setText(sdf.format(new Date(msg.getTimestamp().longValue() * 1000)));
        ui.bodyText.setText(msg.getBody());
        ui.titleEdit.setText(msg.getTitle());
    }

    @SuppressWarnings("unused")
    private void fillMessage(QModelIndex index) {
        fillMessage(index.row());
    }

    @SuppressWarnings("unused")
    private void on_deleteButton_clicked() {
        if (ui.messagesView.selectionModel().selectedIndexes().size() > 0) {
            LinkedList<Message> msgs = new LinkedList<Message>();
            for (QModelIndex index : ui.messagesView.selectionModel().selectedIndexes()) {
                msgs.add(messages.getMessage().get(index.row()));
            }
            messages.getMessage().removeAll(msgs);
            ui.messagesView.setModel(new Model());
            modified = true;
        }
    }

    @SuppressWarnings("unused")
    private void on_closeButton_clicked() {
        close();
    }
}
