package org.second.life.mock;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The xml schema for base test plan
 * 
 * <pre>
 * &lt;TestPlan name=&quot;&quot; impClass=&quot;&quot;&gt; 
 *           &lt;contract-list&gt;
 *            &lt;contract&gt;
 *                 &lt;!-- not used yet --&gt;
 *                 &lt;precondition&gt;
 *                 &lt;/precondition&gt;
 *                 &lt;method name=/&gt;
 *                 &lt;input&gt;
 *                     &lt;!-- specify  --&gt;
 *                     &lt;parameter name= type= value=/&gt;
 *                     &lt;parameter name= type=&gt;&lt;objectref refid=/&gt; &lt;/parameter&gt;
 *                 &lt;/input&gt;
 *                 
 *                 &lt;return&gt;
 *                     &lt;!-- refid can be from input parameter or data section --&gt;
 *                    &lt;parameter name= type= value=/&gt;
 *                     &lt;parameter name= type=&gt;&lt;objectref refid=/&gt; &lt;/parameter&gt;
 *                 &lt;/return&gt;
 *                 &lt;exception class= message= errorcode=&gt;
 *                 &lt;/exception&gt;
 *            &lt;/contract&gt; 
 *            &lt;/contract-list&gt;
 *           
 *       &lt;/TestPlan&gt;
 * </pre>
 **/
public class BaseTestPlan implements TestPlan {

    private List<Contract> contracts;

    public BaseTestPlan() {
        contracts = new ArrayList<Contract>();
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void init(String file) {
        new BaseTestPlanParser().parse(file);
    }

    public void reset() {
        throw new RuntimeException("not implemented yet");
    }

    public void resolve(ObjectRefResolver resolver) {
        for (Contract contract : contracts) {
            contract.resolve(resolver);
        }
    }

    public Result test(Method method, Object[] values) {
        for (Contract contract : contracts) {
            if (contract.match(method, values)) {
                return contract.getResult();
            }
        }
        return null;
    }

    private class BaseTestPlanParser extends DefaultHandler {

        private BaseContract curContract;

        private Parameter curParameter;

        private Result curResult;

        private boolean isResult;

        public void parse(String file) {
            InputStream fis = null;
            try {
                fis = getClass().getResourceAsStream(file);
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                parser.parse(fis, this);
            } catch (Exception e) {
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        public void startElement(String uri, String name, String qName, Attributes attr) throws SAXException {
            if (qName.equals("contract")) {
                curContract = new BaseContract();
                contracts.add(curContract);
            }
            if (qName.equals("method")) {
                curContract.setMethod(attr.getValue("name"));
            }
            if (qName.equals("parameter")) {
                curParameter = new Parameter();
                curParameter.setName(attr.getValue("name"));
                curParameter.setType(attr.getValue("type"));
                String value = attr.getValue("value");
                if (value != null) {
                    curParameter.setValue(new Value(value));
                }
                curContract.addParameter(curParameter);
            }
            if (qName.equals("return")) {
                isResult = true;
            }
            if (qName.equals("result")) {
                curResult = new BaseResult();
                curResult.setName(attr.getValue("name"));
                curResult.setType(attr.getValue("type"));
                String value = attr.getValue("value");
                if (value != null) {
                    curResult.setValue(new Value(value));
                }
                curContract.setResult(curResult);
            }
            if (qName.equals("objectref")) {
                ObjectRef ref = new ObjectRef();
                ref.setRefid(attr.getValue("refid"));
                if (curParameter != null) {
                    curParameter.setValue(new Value(ref));
                }
                if (curResult != null) {
                    curResult.setValue(new Value(ref));
                }
            }
            if (qName.equals("exception")) {
                if (curResult == null) {
                    curResult = new BaseResult();
                    curContract.setResult(curResult);
                }
                MockException exception = new MockException();
                try {
                    exception.setClazz(Class.forName(attr.getValue("class")));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                exception.setMessage(attr.getValue("message"));
                exception.setErrorCode(attr.getValue("errorCode"));
                if (curResult != null) {
                    curResult.setException(exception);
                }
            }
        }

        public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException {
            if (qName.equals("parameter")) {
                curParameter = null;
            }
            if (qName.equals("return")) {
                curResult = null;
                isResult = false;
            }
        }
    }
}
