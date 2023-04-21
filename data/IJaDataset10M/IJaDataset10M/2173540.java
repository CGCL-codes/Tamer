package org.joy.analyzer.scoring;

import org.joy.analyzer.*;
import java.util.List;
import java.util.Set;

/**
 * 打分器抽象类，继承它的子类给关键词提取器提取到的关键词依据一定的算法打分
 * @author Administrator
 */
public abstract class Scorer {

    protected List<Paragraph> paragraphs;

    protected Set<String> termSet;

    protected String fulltext = "";

    public Scorer() {
    }

    /**
     * 设置关键词集合，以用与分数分析，如果需要的话
     * @param termSet 关键词集合
     */
    public void setTermSet(Set<String> termSet) {
        this.termSet = termSet;
    }

    /**
     * 设置段落集合，以用与分数分析，如果需要的话
     * @param paragraphs 段落集合
     */
    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
        for (Paragraph p : paragraphs) {
            fulltext += p.getText();
        }
    }

    /**
     * 获取指定关键词的分数，继承这个类的子类需要实现这个方法
     * @param term 关键词
     * @return 指定关键词的分数
     */
    public abstract double getScore(String term);
}
