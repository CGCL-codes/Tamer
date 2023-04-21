package fr.upmf.animaths.client.mvp;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import fr.upmf.animaths.client.mvp.MathML.MMLMath;

public class MOStaticDisplay extends Composite implements MOStaticPresenter.Display {

    protected MMLMath wrapper;

    public MOStaticDisplay() {
        wrapper = new MMLMath(true);
        initWidget(wrapper);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void startProcessing() {
    }

    @Override
    public void stopProcessing() {
    }

    @Override
    public MMLMath getWrapper() {
        return wrapper;
    }

    @Override
    public void clearWrapper() {
        while (getElement().hasChildNodes()) getElement().removeChild(getElement().getFirstChildElement());
    }
}
