package core.fetcher.example;

import java.util.List;
import com.novse.segmentation.core.statistic.basic.DoubleSetpointPostProcessor;
import com.novse.segmentation.core.statistic.basic.SetpointPostProcessor;
import com.novse.segmentation.core.statistic.fetcher.BasicStatisticStringFetcher;

/**
 * @author Mac Kwan ���ڻ�ͳ�Ƶĸ�Ƶ�ַ��ȡ��
 */
public class BasicStatisticStringFetcherExample {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SetpointPostProcessor setPointPostProcessor = new SetpointPostProcessor();
        setPointPostProcessor.setSetpoint(0.55);
        DoubleSetpointPostProcessor doubleSetPointPostProcessor = new DoubleSetpointPostProcessor();
        doubleSetPointPostProcessor.setMaxConfidence(0.8);
        doubleSetPointPostProcessor.setMinConfidence(0.2);
        List<String> result = null;
        BasicStatisticStringFetcher basic = new BasicStatisticStringFetcher(doubleSetPointPostProcessor);
        result = basic.fileFetch("news.txt");
        for (String s : result) {
            System.out.print(s);
            System.out.print(" ");
        }
        System.out.println();
        result = basic.textFetch("11��29�գ�ԭ����ʦ����ѧУ����ԭ��ͷ��ѧ��ѧ�о������й��ѧԺԺʿ����������ݰ����У����ͨ���Լ����о����ָ���о�������?����ѧ����������Ϊ���ʦ���о���������Ϊ���о����ѧ��ʮ�꡷�ı��档");
        for (String s : result) {
            System.out.print(s);
            System.out.print(" ");
        }
        System.out.println();
    }
}
