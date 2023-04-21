package cn.edu.dutir.test.unit.parser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import org.xml.sax.InputSource;
import cn.edu.dutir.corpus.ntcir.NtcirTopicHandler;
import cn.edu.dutir.corpus.ntcir.NtcirTopicParser;
import com.aliasi.util.Streams;

public class NtcirTopcicParserTest {

    public static void main(String ars[]) throws IOException {
        String docText = "<TOPIC>\r\n<NUM>003</NUM>\r\n<ONUM>NTCIR4-003</ONUM>\r\n<SLANG>CH</SLANG>" + "\r\n<TLANG>CH</TLANG>\r\n<TITLE>��̥��ϸ��</TITLE>\r\n" + "<DESC>��ѯ��ѧ�ҳɹ�����������̥��ϸ�����ؽ���</DESC>\r\n<NARR>\r\n" + "<BACK> 1998������������о�С�����׶���ʵ���ҳɹ�����������̥��ϸ���г�һ�������о�����Ӧ���������ļ������������֯���κ���֯�����ѯ��̥��ϸ����ҽѧ�ϵ�Ӧ�÷�ʽ���乱��Ϊ�Σ��Լ���Ӧ�����Ƿ��������ϵ����飬�������飬������Ϊ�Ρ�</BACK>" + "\r\n<REL>�������Ϊ��̥��ϸ���Ӧ�÷�����ҽѧ���ף��Լ������������ؽ��ܡ���ѧ�ҵ��о��������Ϊ�޹ء�</REL>" + "\r\n</NARR>\r\n<CONC>��̥��ϸ��ҽѧ���ף�Ӧ�÷��������?����</CONC>\r\n</TOPIC>\r\n";
        NtcirTopicParser<NtcirTopicHandler> topicParser = null;
        NtcirTopicHandler topichandler = null;
        StringReader reader = null;
        InputSource inSrc = null;
        try {
            topicParser = new NtcirTopicParser<NtcirTopicHandler>("</TOPIC>");
            topichandler = new NtcirTopicWriterHandler(new OutputStreamWriter(System.out));
            topicParser.setHandler(topichandler);
            System.out.println(docText + "\r\n");
            reader = new StringReader(docText);
            inSrc = new InputSource(reader);
            topicParser.parse(inSrc);
        } finally {
            Streams.closeReader(reader);
        }
    }

    public static void test_generic() throws IOException {
        String docText = "<TOPIC>\r\n<NUM>003</NUM>\r\n<ONUM>NTCIR4-003</ONUM>\r\n<SLANG>CH</SLANG>" + "\r\n<TLANG>CH</TLANG>\r\n<TITLE>��̥��ϸ��</TITLE>\r\n" + "<DESC>��ѯ��ѧ�ҳɹ�����������̥��ϸ�����ؽ���</DESC>\r\n<NARR>\r\n" + "<BACK> 1998������������о�С�����׶���ʵ���ҳɹ�����������̥��ϸ���г�һ�������о�����Ӧ���������ļ������������֯���κ���֯�����ѯ��̥��ϸ����ҽѧ�ϵ�Ӧ�÷�ʽ���乱��Ϊ�Σ��Լ���Ӧ�����Ƿ��������ϵ����飬�������飬������Ϊ�Ρ�</BACK>" + "\r\n<REL>�������Ϊ��̥��ϸ���Ӧ�÷�����ҽѧ���ף��Լ������������ؽ��ܡ���ѧ�ҵ��о��������Ϊ�޹ء�</REL>" + "\r\n</NARR>\r\n<CONC>��̥��ϸ��ҽѧ���ף�Ӧ�÷��������?����</CONC>\r\n</TOPIC>\r\n";
        NtcirTopicParser topicParser = null;
        NtcirTopicHandler topichandler = null;
        StringReader reader = null;
        InputSource inSrc = null;
        try {
            topicParser = new NtcirTopicParser("</TOPIC>");
            topichandler = new NtcirTopicWriterHandler(new OutputStreamWriter(System.out));
            topicParser.setHandler(topichandler);
            System.out.println(docText + "\r\n");
            reader = new StringReader(docText);
            inSrc = new InputSource(reader);
            topicParser.parse(inSrc);
        } finally {
            Streams.closeReader(reader);
        }
    }
}
