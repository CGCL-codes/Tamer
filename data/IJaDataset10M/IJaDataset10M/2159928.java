package org.apache.webbeans.test.component;

import java.util.List;
import javax.webbeans.Current;
import javax.webbeans.Production;
import javax.webbeans.RequestScoped;
import javax.webbeans.TypeLiteral;

@Production
@RequestScoped
public class InjectedTypeLiteralComponent {

    @Current
    private ITypeLiteralComponent<List<String>> component;

    public InjectedTypeLiteralComponent() {
        super();
    }

    /**
	 * @return the component
	 */
    public ITypeLiteralComponent<List<String>> getComponent() {
        return component;
    }

    /**
	 * @param component the component to set
	 */
    public void setComponent(ITypeLiteralComponent<List<String>> component) {
        this.component = component;
    }
}
