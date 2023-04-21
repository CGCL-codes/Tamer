package be.novelfaces.component.webdriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import be.novelfaces.showcase.webdriver.util.WaitForElementCleared;
import be.novelfaces.webdriver.BaseComponent;

public class NumpadDecSep extends BaseComponent {

    private final By byForInput;

    public NumpadDecSep(By byForDecSep, By byForInput) {
        super(byForDecSep);
        this.byForInput = byForInput;
    }

    public CharSequence decimalSeparatorKey() {
        return Keys.DECIMAL;
    }

    public String getValue() {
        return getInputElement().getAttribute("value");
    }

    public void setValue(CharSequence... value) {
        getInputElement().clear();
        new WaitForElementCleared() {

            @Override
            public By getBy() {
                return byForInput;
            }
        };
        getInputElement().sendKeys(value);
    }

    private WebElement getInputElement() {
        return getWebDriver().findElement(byForInput);
    }
}
