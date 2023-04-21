package cn.ekuma.epos.finance;

import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.I_SaveProvider;
import com.openbravo.data.user.I_ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.base.AppLocal;
import com.openbravo.pos.panels.JPanelTable;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Administrator
 */
public class JCashBookPanel extends JPanelTable {

    JCashBookEditor editor;

    I_ListProvider listProvider;

    @Override
    public void init() {
        editor = new JCashBookEditor();
        editor.init(app, this);
        listProvider = new ListProviderCreator(new StaticSentence(app.getSession(), "SELECT HOST, HOSTSEQUENCE, DATESTART, DATEEND,NAME,MONEY  FROM CLOSEDCASH WHERE DATEEND IS NULL ", null, new SerializerReadBasic(new Datas[] { Datas.STRING, Datas.INT, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.STRING })));
    }

    @Override
    public EditorRecord getEditor() {
        return editor;
    }

    @Override
    public I_ListProvider getListProvider() {
        return listProvider;
    }

    @Override
    public I_SaveProvider getSaveProvider() {
        return null;
    }

    @Override
    public String getTitle() {
        return AppLocal.getIntString("Menu.CashBook");
    }

    @Override
    public ListCellRenderer getListCellRenderer() {
        return new DefaultListCellRenderer() {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
                Object[] values = (Object[]) value;
                setText((String) values[0] + "/" + (String) values[4]);
                return this;
            }
        };
    }
}
