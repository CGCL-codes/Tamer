package nl.alterra.openmi.sdk.backbone;

import junit.framework.TestCase;
import nl.alterra.openmi.sdk.extensions.IArguments;
import nl.alterra.openmi.sdk.extensions.IInputExchangeItemEx;
import nl.alterra.openmi.sdk.extensions.IOutputExchangeItemEx;
import org.openmi.standard.ILinkableComponent;
import org.openmi.standard.IQuantity;
import org.openmi.standard.ITimeSpan;
import org.openmi.standard.ITimeStamp;

public class TestExchangeItem extends TestCase {

    ExchangeItem exchangeItem;

    ILinkableComponent component;

    protected class LocalTestComponent extends LinkableComponent {

        public LocalTestComponent(String ID) {
            super(ID);
        }

        public ITimeSpan getTimeHorizon() {
            return null;
        }

        public ITimeStamp getEarliestInputTime() {
            return null;
        }

        public String getComponentID() {
            return null;
        }

        public String getModelDescription() {
            return null;
        }

        public String getModelID() {
            return null;
        }

        public IArguments getInitialisationArguments() {
            return null;
        }

        public IInputExchangeItemEx getInputExchangeItem(String id) {
            return null;
        }

        public IOutputExchangeItemEx getOutputExchangeItem(String id) {
            return null;
        }

        public String validate() {
            return null;
        }

        public String getComponentDescription() {
            return null;
        }
    }

    public void setUp() {
        component = new LocalTestComponent("LocalTestComponent");
        exchangeItem = new ExchangeItem(component, "");
        exchangeItem.setDataType(new Quantity("Q"));
        ElementSet elementSet = new ElementSet();
        elementSet.setID("ES");
        exchangeItem.setElementSet(elementSet);
    }

    public void testLinkableComponent() {
        assertEquals(component, exchangeItem.getOwner());
    }

    public void testElementSet() {
        ElementSet elementSet = new ElementSet();
        elementSet.setID("ES");
        assertTrue(elementSet.equals(exchangeItem.getElementSet()));
    }

    public void testDataType() {
        assertTrue(exchangeItem.getDataType().equals(new Quantity("Q")));
        assertTrue(new InputExchangeItem(null, "dummy").getDataType() instanceof IQuantity);
    }

    public void testEquals() {
        ExchangeItem exchangeItem = new ExchangeItem(component, "");
        exchangeItem.setDataType(new Quantity("Q"));
        ElementSet elementSet = new ElementSet();
        elementSet.setID("ES");
        exchangeItem.setElementSet(elementSet);
        assertTrue(exchangeItem.equals(this.exchangeItem));
        exchangeItem.setDataType(new Quantity("Q1"));
        assertTrue(exchangeItem.equals(this.exchangeItem));
        exchangeItem.setDataType(new Quantity("Q"));
        elementSet.setID("ES2");
        assertTrue(exchangeItem.equals(this.exchangeItem));
        assertFalse(exchangeItem.equals(null));
        assertFalse(exchangeItem.equals("string"));
    }
}
