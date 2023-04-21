package net.sf.osadm.linedata;

import java.util.ArrayList;
import java.util.List;
import net.sf.osadm.linedata.domain.TableData;

public class LineDataMessageStoreImpl implements LineDataMessageStore, LineDataMessageItemVisitorAcceptor {

    private List<LineDataMessageItem> messageItemList;

    public LineDataMessageStoreImpl() {
        super();
        this.messageItemList = new ArrayList<LineDataMessageItem>();
    }

    public void addMessage(TableData lineData, String fieldName, String priority, String message, Throwable cause) {
        messageItemList.add(new LineDataMessageItemImpl(lineData, fieldName, priority, message, cause));
    }

    public boolean isEmpty() {
        return messageItemList.isEmpty();
    }

    public void accept(LineDataMessageItemVisitor visitor) {
        for (LineDataMessageItem messageItem : messageItemList) {
            visitor.visit(messageItem);
        }
    }
}
