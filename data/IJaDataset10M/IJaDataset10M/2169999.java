package cn.edu.dutir.corpus.trec;

import cn.edu.dutir.corpus.TopicHandler;

public interface TrecTopicHandler extends TopicHandler<TrecTopic> {

    @Override
    public abstract void handle(TrecTopic topic);
}
