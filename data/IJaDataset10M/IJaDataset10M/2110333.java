package test;

import hrc.tool.converter.AbstractConvert;
import hrc.tool.converter.ConvertException;
import hrc.tool.converter.Formatter;
import hrc.tool.converter.xml.XmlConvert;
import hrc.tool.converter.xml.XmlMultiFormatter;
import hrc.tool.converter.xml.XmlSingleFormatter;
import hrc.tool.xml.XmlException;
import hrc.tool.xml.XmlTeller;
import hrc.tool.xml.XmlTellerFactory;
import java.util.List;
import test.base.Child;
import test.base.Parent;
import test.base.ParentWithChildren;
import test.base.Person;
import test.base.XmlBeanConverterTest;

/**
 * xml��ת�����Ժ�excel����ͬ����ͬ������֧�ָ���vo��ת��
 * @author hrc
 *
 */
public class XmlConvertTest extends XmlBeanConverterTest {

    private AbstractConvert convert;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        convert = new XmlConvert();
    }

    @Override
    protected void convert(Object o, String fileName) throws XmlException {
        try {
            Formatter formatter = convert.buildFormatter(o);
            convert.save(fileName);
            XmlTeller teller = (XmlTeller) formatter.getFormatterBean();
            System.out.println(teller.toString());
        } catch (ConvertException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testSimpleFormatToBean() throws XmlException {
        XmlTeller teller = XmlTellerFactory.createOldFileXmlTeller("1.xml");
        Formatter formatter = new XmlSingleFormatter(teller, "//person");
        try {
            Person person = convert.getBean(Person.class, formatter);
            System.out.println(person.getId());
            System.out.println(person.getAge());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testListFormatToBean() throws XmlException {
        XmlTeller teller = XmlTellerFactory.createOldFileXmlTeller("2.xml");
        Formatter formatter = new XmlMultiFormatter(teller, "//persons");
        try {
            List<Person> persons = convert.getBeans(Person.class, formatter);
            for (Person person : persons) {
                System.out.println(person);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testMultiFormatToBean() throws XmlException {
        XmlTeller teller = XmlTellerFactory.createOldFileXmlTeller("3.xml");
        Formatter formatter = new XmlSingleFormatter(teller, "//parent");
        try {
            Parent person = convert.getBean(Parent.class, formatter);
            System.out.println(person);
            System.out.println(person.getChild());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testComplexFormatToBean() throws XmlException {
        XmlTeller teller = XmlTellerFactory.createOldFileXmlTeller("4.xml");
        Formatter formatter = new XmlSingleFormatter(teller, "//parentWithChildren");
        try {
            ParentWithChildren person = convert.getBean(ParentWithChildren.class, formatter);
            System.out.println(person);
            for (Child child : person.getChildren()) {
                System.out.println(child);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
