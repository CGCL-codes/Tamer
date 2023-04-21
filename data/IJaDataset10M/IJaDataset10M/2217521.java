package test.ws.prova.test2;

import java.io.IOException;
import java.util.List;
import org.junit.Test;
import ws.prova.kernel2.ProvaKnowledgeBase;
import ws.prova.kernel2.ProvaResolutionInferenceEngine;
import ws.prova.kernel2.ProvaResultSet;
import ws.prova.kernel2.ProvaRule;
import ws.prova.parser2.ProvaParserImpl;
import ws.prova.parser2.ProvaParsingException;
import ws.prova.reference2.ProvaKnowledgeBaseImpl;
import ws.prova.reference2.ProvaResolutionInferenceEngineImpl;
import ws.prova.reference2.ProvaResultSetImpl;

public class ProvaCacheTest {

    public void cache_loop10() {
        ProvaKnowledgeBase kb = new ProvaKnowledgeBaseImpl();
        ProvaResultSet resultSet = new ProvaResultSetImpl();
        ProvaParserImpl parser = new ProvaParserImpl("rules/prova-examples/loop10.prova", new Object[] {});
        try {
            List<ProvaRule> rules = parser.parse(kb, resultSet, "rules/prova-examples/loop10.prova");
            int[] numSolutions = new int[] { 0 };
            int i = 0;
            for (ProvaRule rule : rules) {
                if (rule.getHead() == null) {
                    ProvaResolutionInferenceEngine engine = new ProvaResolutionInferenceEngineImpl(kb, rule);
                    engine.run();
                    org.junit.Assert.assertEquals(resultSet.getSolutions().size(), numSolutions[i++]);
                    resultSet.getSolutions().clear();
                }
            }
        } catch (ProvaParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cache_loop8() {
        ProvaKnowledgeBase kb = new ProvaKnowledgeBaseImpl();
        ProvaResultSet resultSet = new ProvaResultSetImpl();
        ProvaParserImpl parser = new ProvaParserImpl("rules/prova-examples/loop8.prova", new Object[] {});
        try {
            List<ProvaRule> rules = parser.parse(kb, resultSet, "rules/prova-examples/loop8.prova");
            int[] numSolutions = new int[] { 4 };
            int i = 0;
            for (ProvaRule rule : rules) {
                if (rule.getHead() == null) {
                    ProvaResolutionInferenceEngine engine = new ProvaResolutionInferenceEngineImpl(kb, rule);
                    engine.run();
                    org.junit.Assert.assertEquals(resultSet.getSolutions().size(), numSolutions[i++]);
                    resultSet.getSolutions().clear();
                }
            }
        } catch (ProvaParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cache_loop7() {
        ProvaKnowledgeBase kb = new ProvaKnowledgeBaseImpl();
        ProvaResultSet resultSet = new ProvaResultSetImpl();
        ProvaParserImpl parser = new ProvaParserImpl("rules/prova-examples/loop7.prova", new Object[] {});
        try {
            List<ProvaRule> rules = parser.parse(kb, resultSet, "rules/prova-examples/loop7.prova");
            int[] numSolutions = new int[] { 8 };
            int i = 0;
            for (ProvaRule rule : rules) {
                if (rule.getHead() == null) {
                    ProvaResolutionInferenceEngine engine = new ProvaResolutionInferenceEngineImpl(kb, rule);
                    engine.run();
                    org.junit.Assert.assertEquals(resultSet.getSolutions().size(), numSolutions[i++]);
                    resultSet.getSolutions().clear();
                }
            }
        } catch (ProvaParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cache_loop6() {
        ProvaKnowledgeBase kb = new ProvaKnowledgeBaseImpl();
        ProvaResultSet resultSet = new ProvaResultSetImpl();
        ProvaParserImpl parser = new ProvaParserImpl("rules/prova-examples/loop6.prova", new Object[] {});
        try {
            List<ProvaRule> rules = parser.parse(kb, resultSet, "rules/prova-examples/loop6.prova");
            int[] numSolutions = new int[] { 2 };
            int i = 0;
            for (ProvaRule rule : rules) {
                if (rule.getHead() == null) {
                    ProvaResolutionInferenceEngine engine = new ProvaResolutionInferenceEngineImpl(kb, rule);
                    engine.run();
                    org.junit.Assert.assertEquals(resultSet.getSolutions().size(), numSolutions[i++]);
                    resultSet.getSolutions().clear();
                }
            }
        } catch (ProvaParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cache_loop5() {
        ProvaKnowledgeBase kb = new ProvaKnowledgeBaseImpl();
        ProvaResultSet resultSet = new ProvaResultSetImpl();
        ProvaParserImpl parser = new ProvaParserImpl("rules/prova-examples/loop5.prova", new Object[] {});
        try {
            List<ProvaRule> rules = parser.parse(kb, resultSet, "rules/prova-examples/loop5.prova");
            int[] numSolutions = new int[] { 4 };
            int i = 0;
            for (ProvaRule rule : rules) {
                if (rule.getHead() == null) {
                    ProvaResolutionInferenceEngine engine = new ProvaResolutionInferenceEngineImpl(kb, rule);
                    engine.run();
                    org.junit.Assert.assertEquals(resultSet.getSolutions().size(), numSolutions[i++]);
                    resultSet.getSolutions().clear();
                }
            }
        } catch (ProvaParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cache_loop() {
        ProvaKnowledgeBase kb = new ProvaKnowledgeBaseImpl();
        ProvaResultSet resultSet = new ProvaResultSetImpl();
        ProvaParserImpl parser = new ProvaParserImpl("rules/prova-examples/loop.prova", new Object[] {});
        try {
            List<ProvaRule> rules = parser.parse(kb, resultSet, "rules/prova-examples/loop.prova");
            int[] numSolutions = new int[] { 2 };
            int i = 0;
            for (ProvaRule rule : rules) {
                if (rule.getHead() == null) {
                    ProvaResolutionInferenceEngine engine = new ProvaResolutionInferenceEngineImpl(kb, rule);
                    engine.run();
                    org.junit.Assert.assertEquals(resultSet.getSolutions().size(), numSolutions[i++]);
                    resultSet.getSolutions().clear();
                }
            }
        } catch (ProvaParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cache_loop2() {
        ProvaKnowledgeBase kb = new ProvaKnowledgeBaseImpl();
        ProvaResultSet resultSet = new ProvaResultSetImpl();
        ProvaParserImpl parser = new ProvaParserImpl("rules/prova-examples/loop2.prova", new Object[] {});
        try {
            List<ProvaRule> rules = parser.parse(kb, resultSet, "rules/prova-examples/loop2.prova");
            int[] numSolutions = new int[] { 3 };
            int i = 0;
            for (ProvaRule rule : rules) {
                if (rule.getHead() == null) {
                    ProvaResolutionInferenceEngine engine = new ProvaResolutionInferenceEngineImpl(kb, rule);
                    engine.run();
                    org.junit.Assert.assertEquals(resultSet.getSolutions().size(), numSolutions[i++]);
                    resultSet.getSolutions().clear();
                }
            }
        } catch (ProvaParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cache_loop3() {
        ProvaKnowledgeBase kb = new ProvaKnowledgeBaseImpl();
        ProvaResultSet resultSet = new ProvaResultSetImpl();
        ProvaParserImpl parser = new ProvaParserImpl("rules/prova-examples/loop3.prova", new Object[] {});
        try {
            List<ProvaRule> rules = parser.parse(kb, resultSet, "rules/prova-examples/loop3.prova");
            int[] numSolutions = new int[] { 4 };
            int i = 0;
            for (ProvaRule rule : rules) {
                if (rule.getHead() == null) {
                    ProvaResolutionInferenceEngine engine = new ProvaResolutionInferenceEngineImpl(kb, rule);
                    engine.run();
                    org.junit.Assert.assertEquals(resultSet.getSolutions().size(), numSolutions[i++]);
                    resultSet.getSolutions().clear();
                }
            }
        } catch (ProvaParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cache_loop4() {
        ProvaKnowledgeBase kb = new ProvaKnowledgeBaseImpl();
        ProvaResultSet resultSet = new ProvaResultSetImpl();
        ProvaParserImpl parser = new ProvaParserImpl("rules/prova-examples/loop4.prova", new Object[] {});
        try {
            List<ProvaRule> rules = parser.parse(kb, resultSet, "rules/prova-examples/loop4.prova");
            int[] numSolutions = new int[] { 8 };
            int i = 0;
            for (ProvaRule rule : rules) {
                if (rule.getHead() == null) {
                    ProvaResolutionInferenceEngine engine = new ProvaResolutionInferenceEngineImpl(kb, rule);
                    engine.run();
                    org.junit.Assert.assertEquals(resultSet.getSolutions().size(), numSolutions[i++]);
                    resultSet.getSolutions().clear();
                }
            }
        } catch (ProvaParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
