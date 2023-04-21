package org.istcontract.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import net.sf.istcontract.aws.input.contract.Contract;

public class ContractParser {

    public static Contract getContractFromFile(String stFileName) throws JAXBException {
        JAXBContext jc;
        Unmarshaller u;
        Contract ct;
        jc = JAXBContext.newInstance(Contract.class);
        u = jc.createUnmarshaller();
        ct = (Contract) ((JAXBElement) u.unmarshal(new File(stFileName))).getValue();
        return ct;
    }

    public static Contract getContractFromXml(String xml) throws JAXBException {
        JAXBContext jc;
        Unmarshaller u;
        Contract ct;
        ByteArrayInputStream bais;
        jc = JAXBContext.newInstance(Contract.class);
        u = jc.createUnmarshaller();
        bais = new ByteArrayInputStream(xml.getBytes());
        ct = (Contract) u.unmarshal(bais);
        try {
            bais.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ct;
    }

    public static void getXmlFromContract(Contract ct, OutputStream os) throws JAXBException {
        JAXBContext jc;
        Marshaller m;
        jc = JAXBContext.newInstance(Contract.class);
        m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
        m.marshal(ct, os);
    }

    public static void main(String args[]) throws JAXBException {
        Contract ct;
        if (args.length >= 1) {
            ct = ContractParser.getContractFromXml(args[0]);
            System.out.println(ct.getContractName());
            ContractParser.getXmlFromContract(ct, System.out);
        }
    }
}
