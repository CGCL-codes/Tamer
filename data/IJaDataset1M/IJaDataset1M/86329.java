package core.unlistedword.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.novse.segmentation.core.unlistedword.SimpleUnListedWordAnalyzer;
import com.novse.segmentation.core.unlistedword.dictionary.CharFrequenceDictionary;

/**
 * @author Mac Kwan δ��¼��ʶ�������
 */
public class SimpleUnListedWordAnalyzerExample {

    /**
     * @param args
     */
    public static void main(String[] args) {
        CharFrequenceDictionary frequenceDic = new CharFrequenceDictionary();
        frequenceDic.loadDictionary("Dic/Txt/��Ƶ�ʿ�.txt");
        SimpleUnListedWordAnalyzer analyzer = new SimpleUnListedWordAnalyzer(frequenceDic);
        String source = "11��29�գ�ԭ����ʦ����ѧУ����ԭ��ͷ��ѧ��ѧ�о������й��ѧԺԺʿ����������ݰ����У����ͨ���Լ����о����ָ���о�������?����ѧ����������Ϊ���ʦ���о���������Ϊ���о����ѧ��ʮ�꡷�ı��档";
        String[] array = source.split("");
        List<String> result = new ArrayList<String>(Arrays.asList(array));
        for (String s : result) {
            System.out.print(s);
            System.out.print(" ");
        }
        System.out.println();
        result = analyzer.identify(result);
        for (String s : result) {
            System.out.print(s);
            System.out.print(" ");
        }
    }
}
