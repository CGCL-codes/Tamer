package anima.component.view;

import java.awt.Window;
import anima.annotation.ComponentInterface;
import anima.component.ISupports;

@ComponentInterface("http://purl.org/NET/dcc/anima.component.view.IWRoot")
public interface IWRoot extends IWComponent, ISupports {

    public Window getWidget();
}
