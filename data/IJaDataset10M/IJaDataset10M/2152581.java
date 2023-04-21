package org.alicebot.server.core.processor;

/**
 *  Registers {@link AIMLProcessor}s for a given version of AIML.
 *
 *  @since  4.1.3
 *  @author Noel Bush
 */
public class AIMLProcessorRegistry extends ProcessorRegistry {

    /** The version of AIML for which this registry is intended. */
    private static final String version = "1.0.1";

    /** The list of processors (fully-qualified class names). */
    private static final String[] processorList = { "org.alicebot.server.core.processor.BotProcessor", "org.alicebot.server.core.processor.ConditionProcessor", "org.alicebot.server.core.processor.DateProcessor", "org.alicebot.server.core.processor.FormalProcessor", "org.alicebot.server.core.processor.GenderProcessor", "org.alicebot.server.core.processor.GetProcessor", "org.alicebot.server.core.processor.GossipProcessor", "org.alicebot.server.core.processor.IDProcessor", "org.alicebot.server.core.processor.InputProcessor", "org.alicebot.server.core.processor.JavaScriptProcessor", "org.alicebot.server.core.processor.LearnProcessor", "org.alicebot.server.core.processor.LowerCaseProcessor", "org.alicebot.server.core.processor.Person2Processor", "org.alicebot.server.core.processor.PersonProcessor", "org.alicebot.server.core.processor.RandomProcessor", "org.alicebot.server.core.processor.SentenceProcessor", "org.alicebot.server.core.processor.SetProcessor", "org.alicebot.server.core.processor.SizeProcessor", "org.alicebot.server.core.processor.SRAIProcessor", "org.alicebot.server.core.processor.SRProcessor", "org.alicebot.server.core.processor.StarProcessor", "org.alicebot.server.core.processor.SystemProcessor", "org.alicebot.server.core.processor.ThatProcessor", "org.alicebot.server.core.processor.ThatStarProcessor", "org.alicebot.server.core.processor.ThinkProcessor", "org.alicebot.server.core.processor.TopicStarProcessor", "org.alicebot.server.core.processor.UpperCaseProcessor", "org.alicebot.server.core.processor.VersionProcessor" };

    /** The fully-qualified name of {@link AIMLProcessor}. */
    private static final String processorBaseClassName = "org.alicebot.server.core.processor.AIMLProcessor";

    /** The name of the field in an {@link AIMLProcessor} that contains the label. */
    public static final String labelFieldName = "label";

    public AIMLProcessorRegistry() {
        super(version, processorList, processorBaseClassName, labelFieldName);
    }
}
