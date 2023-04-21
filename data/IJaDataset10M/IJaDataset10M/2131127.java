package jifx.connection.translator.regExTranslator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jifx.commons.mapper.ejb.IMapper;
import jifx.commons.messages.IMessage;
import jifx.connection.translator.TranslateException;
import jifx.connection.translator.generics.AbstractTranslator;
import jifx.message.ifx.IFXMessageFactory;
import jifx.message.ifx.IFXPackager;
import jifx.message.iso8583.ISOMessageFactory;
import jifx.message.iso8583.ISOPackager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 *	<translator class="jifx.connection.translator.ifx_iso.DebitAddRq_CWD_date">
 *		<property key="11" value="(..)(..)" />
 *		<property key="12" value="(..)(..)" />
 *		<property key="a.b.c" value="{0,0}hola{0,1}-{1,0}-{1,1}" />
 *	</translator>
 *
 * @author jbochard
 *
 */
public class RegExTranslator extends AbstractTranslator {

    static Logger logger = Logger.getLogger(RegExTranslator.class);

    protected List<ProgramEntry> program;

    @Override
    public void configure(Element element) {
        super.configure(element);
        program = new ArrayList<ProgramEntry>();
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Element.ELEMENT_NODE) {
                Element property = (Element) nl.item(i);
                if (property.getNodeName().equals("get")) {
                    String source = property.getAttribute("source");
                    String pattern = property.getAttribute("pattern");
                    String name = property.getAttribute("name");
                    program.add(new GetPropertyValue(source, pattern, name));
                }
                if (property.getNodeName().equals("set")) {
                    String dest = property.getAttribute("dest");
                    String format = property.getAttribute("format");
                    program.add(new SetPropertyValue(dest, format));
                }
            }
        }
    }

    public IMessage translate(Map<String, Object> context, IMapper mapper, IMessage orig, IMessage proc) throws TranslateException {
        Map<String, String[]> variables = new HashMap<String, String[]>();
        for (ProgramEntry pv : program) {
            if (logger.isDebugEnabled()) logger.debug("Aplicando traductor - " + pv.toString());
            pv.process(variables, required, context, mapper, orig, proc);
        }
        return proc;
    }

    public static void main(String[] args) {
        IMessage orig = new ISOPackager().unpack(ISOMessageFactory.getISOPOSRequest("100"));
        IMessage dest = new IFXPackager().unpack(IFXMessageFactory.getIfxCert());
        Map<String, Object> context = new HashMap<String, Object>();
        System.out.println(orig);
        List<ProgramEntry> parameters = new ArrayList<ProgramEntry>();
        parameters.add(new GetPropertyValue("11", "((\\d)(\\d+))", "juan"));
        parameters.add(new SetPropertyValue("BankSvcRq.DebitAddRq.MsgRqHdr.ClientTerminalSeqId", "$juan(0)"));
        try {
            RegExTranslator trans = new RegExTranslator();
            trans.setProgram(parameters);
            trans.translate(context, null, orig, dest);
        } catch (TranslateException e) {
            e.printStackTrace();
        }
        parameters = new ArrayList<ProgramEntry>();
        parameters.add(new GetPropertyValue("17", "(\\d\\d)(\\d\\d)", "juan"));
        parameters.add(new GetPropertyValue("$F{sysdate}", "(\\d\\d\\d\\d).*", "lolo"));
        parameters.add(new SetPropertyValue("SignonRs.ServerDt", "$lolo(0)-$juan(0)-$juan(1)"));
        try {
            RegExTranslator trans = new RegExTranslator();
            trans.setProgram(parameters);
            trans.translate(context, null, orig, dest);
        } catch (TranslateException e) {
            e.printStackTrace();
        }
        System.out.println(dest);
    }

    public List<ProgramEntry> getProgram() {
        return program;
    }

    public void setProgram(List<ProgramEntry> program) {
        this.program = program;
    }
}
