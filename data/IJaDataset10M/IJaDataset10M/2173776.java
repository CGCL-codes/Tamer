package com.novse.segmentation.proxy;

import java.util.List;
import com.novse.segmentation.core.AbstractSegmentProcessor;
import com.novse.segmentation.core.SegmentProcessor;
import com.novse.segmentation.core.unlistedword.UnListedWordAnalyzer;

/**
 * @author Mac Kwan ʹ��δ��¼��ʶ���������������Ļ��ڴʵ�ƥ������ķִʴ���
 */
public class SegmentProcessorUnListedWordProxy extends AbstractSegmentProcessor {

    private SegmentProcessor target = null;

    private UnListedWordAnalyzer analyzer = null;

    /**
     * Ĭ�Ϲ��캯��
     * 
     * @param target
     *            ������Ķ���
     * @param analyzer
     *            δ��¼��ʶ�������
     */
    public SegmentProcessorUnListedWordProxy(SegmentProcessor target, UnListedWordAnalyzer analyzer) {
        this.target = target;
        this.analyzer = analyzer;
    }

    @Override
    public List<String> textProcess(String text) {
        List<String> tempResult = this.target.textProcess(text);
        List<String> result = this.analyzer.identify(tempResult);
        return result;
    }

    public UnListedWordAnalyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(UnListedWordAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public SegmentProcessor getTarget() {
        return target;
    }

    public void setTarget(SegmentProcessor target) {
        this.target = target;
    }
}
