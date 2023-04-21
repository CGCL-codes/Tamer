package org.webchem;

import java.util.ArrayList;
import org.webchem.loc.Locator;
import org.webchem.loc.SelectStrategy;
import org.webchem.loc.SelectionLocator;
import org.webchem.tree.Element;

public interface IWebChemEngine extends IWebChemControl {

    public String getText(Locator locator);

    public boolean setText(Locator locator, String text);

    public String getValue(Locator locator);

    public boolean setValue(Locator locator, String value);

    public boolean selectOption(Locator locator, SelectionLocator choice);

    public String getSelectedOption(Locator locator, SelectStrategy strategy);

    public ArrayList<String> getSelectOptions(Locator locator, SelectStrategy strategy);

    public boolean isElementPresent(Locator locator);

    public boolean isVisible(Locator locator);

    public String getTitle();

    public String getURL();

    public Element getRootElement();

    public boolean setCheckbox(Locator locator);

    public boolean clearChecked(Locator locator);

    public boolean isChecked(Locator locator);

    public boolean setRadio(Locator locator);

    public boolean clearRadio(Locator locator);

    public boolean isRadioSet(Locator locator);

    public boolean click(Locator locator);

    public void setBaseURL(String url);
}
