package com.divosa.eformulieren.toolkit.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Test;
import com.divosa.eformulieren.core.helperobjects.XStreamHelper;
import com.divosa.eformulieren.domain.domeinobject.Field;
import com.divosa.eformulieren.toolkit.renderer.helperobjects.Control;
import com.divosa.eformulieren.toolkit.renderer.helperobjects.Controls;
import com.divosa.eformulieren.toolkit.renderer.helperobjects.XStreamControlsHelper;

/**
 * @author Bart Ottenkamp
 */
public class XStreamTest {

    /**
     * The logger for this class.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Tests the XStream manipulation of Fields.
     */
    @Test
    public void test() {
        Controls controls = new Controls();
        Control control = new Control();
        control.setName("name");
        control.setType("type");
        Option option1 = new Option();
        option1.setId(new Long(1));
        option1.setText("text1");
        option1.setValue("val1");
        Option option2 = new Option();
        option2.setId(new Long(2));
        option2.setText("text2");
        option2.setValue("val2");
        List<Option> options = new ArrayList<Option>();
        options.add(option1);
        options.add(option2);
        Field field1 = new Field();
        field1.setId(new Long("88"));
        field1.setName("nameField1");
        Field field2 = new Field();
        field2.setId(new Long("99"));
        field2.setName("nameField2");
        List<Field> fields = new ArrayList<Field>();
        fields.add(field1);
        fields.add(field2);
        control.setFields(fields);
        controls.setControl(control);
        XStreamHelper xstreamHelper = new XStreamControlsHelper();
        String xml = xstreamHelper.getXmlFromObjectGraph(controls);
        logger.info(xml);
        Controls controlsNew = (Controls) xstreamHelper.getObjectGraphFromXml(xml);
        logger.info(controlsNew);
    }
}
