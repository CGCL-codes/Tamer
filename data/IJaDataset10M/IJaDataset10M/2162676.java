package pages;

import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.html.BasePage;

public abstract class Home extends BasePage {

    @Persist
    public abstract int getCounter();

    public abstract void setCounter(int counter);

    public void doClick(int increment) {
        int counter = getCounter();
        counter += increment;
        setCounter(counter);
    }

    public void doClear() {
        setCounter(0);
    }
}
