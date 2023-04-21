package es.tecnocom.swtclient.ui.secciones.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import es.tecnocom.swtclient.conf.SpringFactory;
import es.tecnocom.swtclient.ui.utils.UICommand;

public class ListenerMenuEjecutarComando implements SelectionListener {

    public void widgetDefaultSelected(SelectionEvent selectionevent) {
    }

    public void widgetSelected(SelectionEvent selectionevent) {
        System.out.println();
        SpringFactory.getInstance().getBean("chorrasCommand", UICommand.class).execute();
    }
}
