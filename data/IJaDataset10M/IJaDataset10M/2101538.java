package br.ufmg.ubicomp.decs.client.ui.callback;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import br.ufmg.ubicomp.decs.client.DECSContext;

public class OptionsOkCallback extends AbstractAsyncCallback {

    public OptionsOkCallback(DECSContext context) {
        super(context);
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        onUpdateMarkerLocation(result);
    }

    private void onUpdateMarkerLocation(String result) {
        Document doc = XMLParser.parse(result);
    }
}
