package org.emar.prepaid.panel;

import javax.swing.ListCellRenderer;
import net.adrianromero.data.gui.ListCellRendererBasic;
import net.adrianromero.data.loader.ComparatorCreator;
import net.adrianromero.tpv.forms.AppLocal;
import net.adrianromero.tpv.panels.JPanelTable;
import net.adrianromero.tpv.ticket.*;
import net.adrianromero.tpv.forms.AppView;
import net.adrianromero.data.loader.TableDefinition;
import net.adrianromero.data.loader.Vectorer;
import net.adrianromero.data.user.EditorRecord;
import net.adrianromero.data.user.SaveProvider;
import net.adrianromero.data.user.ListProvider;
import net.adrianromero.data.user.ListProviderCreator;
import net.adrianromero.tpv.forms.SentenceContainer;
import org.emar.prepaid.editor.ClientEditor;

public class PanelCliente extends JPanelTable {

    private TableDefinition tcliente;

    private ClientEditor jeditor;

    /** Creates a new instance of JPanelDuty */
    public PanelCliente(AppView oApp) {
        super(oApp);
        tcliente = m_App.lookupDataLogic(SentenceContainer.class).getTableClientes();
        jeditor = new ClientEditor(m_Dirty);
    }

    public ListProvider getListProvider() {
        return new ListProviderCreator(tcliente);
    }

    public SaveProvider getSaveProvider() {
        return new SaveProvider(tcliente);
    }

    public Vectorer getVectorer() {
        return tcliente.getVectorerBasic(new int[] { 0, 1, 2, 3 });
    }

    public ComparatorCreator getComparatorCreator() {
        return tcliente.getComparatorCreator(new int[] { 0, 1, 2, 3 });
    }

    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tcliente.getRenderStringBasic(new int[] { 0, 2, 1 }));
    }

    public EditorRecord getEditor() {
        return jeditor;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.Clientes");
    }
}
