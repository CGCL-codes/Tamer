package com.finalist.jaggenerator.modules;

import javax.xml.parsers.*;
import com.finalist.jaggenerator.menu.BasePopup;

/**
 *
 * @author  hillie
 */
public interface JagBean {

    public javax.swing.JPanel getPanel();

    public String getRefName();

    public void getXML(org.w3c.dom.Element el) throws ParserConfigurationException;

    /**
    *  Gets the popup for the current JagBean.
    *  A <b>null</b> value is returned if there is no actions. 
    *  @return <code>com.finalist.jaggenerator.menu.BasePopup</code> 
    * */
    public BasePopup getPopup();
}
