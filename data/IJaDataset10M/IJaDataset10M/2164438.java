package de.jmda.mview.typeshape;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import de.jmda.gui.swing.mvc.ModelBase;

/**
 *
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class DisplayOptionsModel extends ModelBase {

    @XmlEnum
    enum DisplayMode {

        @XmlEnumValue("SHOW")
        SHOW, @XmlEnumValue("HIDE")
        HIDE
    }

    @XmlEnum
    enum DisplayStyle {

        @XmlEnumValue("QUALIFIED")
        QUALIFIED, @XmlEnumValue("SIMPLE")
        SIMPLE, @XmlEnumValue("HIDE")
        HIDE
    }

    @XmlType
    @XmlAccessorType(XmlAccessType.FIELD)
    static class Visibility {

        @XmlAttribute
        boolean bPublic = true;

        @XmlAttribute
        boolean bDefault = true;

        @XmlAttribute
        boolean bProtected = true;

        @XmlAttribute
        boolean bPrivate = true;
    }

    @XmlType
    @XmlAccessorType(XmlAccessType.FIELD)
    static class DisplayOptionsType {

        DisplayStyle displayStyle = DisplayStyle.QUALIFIED;

        DisplayMode displayModeAnnotations = DisplayMode.SHOW;
    }

    @XmlType
    @XmlAccessorType(XmlAccessType.FIELD)
    static class DisplayOptionsFields {

        DisplayMode displayMode = DisplayMode.SHOW;

        DisplayStyle displayStyleTypes = DisplayStyle.SIMPLE;

        DisplayMode displayModeAnnotations = DisplayMode.SHOW;

        Visibility visibility = new Visibility();

        @XmlAttribute
        boolean visibilityAll;
    }

    @XmlType
    @XmlAccessorType(XmlAccessType.FIELD)
    static class DisplayOptionsMethods {

        DisplayMode displayMode = DisplayMode.SHOW;

        DisplayMode displayModeAnnotations = DisplayMode.SHOW;

        DisplayMode displayModeReturnValue = DisplayMode.SHOW;

        DisplayStyle displayStyleReturnValue = DisplayStyle.SIMPLE;

        DisplayMode displayModeReturnValueAnnotations = DisplayMode.SHOW;

        DisplayMode displayModeParameter = DisplayMode.SHOW;

        DisplayStyle displayStyleParameter = DisplayStyle.SIMPLE;

        DisplayMode displayModeParameterAnnotations = DisplayMode.SHOW;

        Visibility visibility = new Visibility();

        @XmlAttribute
        boolean visibilityAll;
    }

    @XmlElement
    DisplayOptionsType displayOptionsType = new DisplayOptionsType();

    @XmlElement
    DisplayOptionsFields displayOptionsFields = new DisplayOptionsFields();

    @XmlElement
    DisplayOptionsMethods displayOptionsMethods = new DisplayOptionsMethods();

    /**
	 * indicates that dialog has been finished by pressing ok
	 */
    @XmlAttribute
    public boolean acknowledged = false;
}
