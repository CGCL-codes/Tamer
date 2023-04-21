package heisig.data;

import heisig.input.KanjiFileReader;
import heisig.random.CardDrawingStrategy;
import heisig.random.LeitnerStrategyEqualSizedGroups;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The data of the application: min and max indexes to query, the currently queried kanji index
 * and the {@link CardDrawingStrategy} which determines the order in which the kanjis are queried.
 *  
 * @author abartho
 */
public class QuizData {

    public static final int MIN_INDEX = 1;

    public static final int MAX_INDEX = 2042;

    private Map<Integer, KanjiFrame> kanjiMap;

    private int min, max, current;

    private boolean queryStrokes;

    private CardDrawingStrategy cardDrawingStrategy;

    public QuizData() {
        KanjiFileReader kfr = new KanjiFileReader();
        kanjiMap = kfr.read();
        cardDrawingStrategy = getStrategyFromProperties();
    }

    public int getCurrent() {
        return current;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean getQueryStrokes() {
        return queryStrokes;
    }

    public void setQueryStrokes(boolean queryStrokes) {
        this.queryStrokes = queryStrokes;
    }

    public int getNrOfKanjis() {
        return kanjiMap.size();
    }

    /**
	 * Gets from the CardDrawingStrategy for the kanji to be asked next
	 *
	 */
    public void drawNext() {
        current = cardDrawingStrategy.getNextKanjiId(min, max);
    }

    public KanjiFrame getCurrentKanjiFrame() {
        return kanjiMap.get(current);
    }

    public void correctGuess(int id) {
        cardDrawingStrategy.correctGuess(id);
    }

    public void wrongGuess(int id) {
        cardDrawingStrategy.wrongGuess(id);
    }

    /**
	 * Makes a DOM representation of the QuizData (to be saved to a file). The CardDrawingStrategy
	 * is also asked if it needs to add information.
	 * 
	 * @return a DOM representation of QuizData's content
	 */
    public Document getStateAsXml() {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element eSave = doc.createElement("save");
            doc.appendChild(eSave);
            Element eData = doc.createElement("data");
            Element eMin = doc.createElement("min");
            eMin.setTextContent(String.valueOf(getMin()));
            Element eMax = doc.createElement("max");
            eMax.setTextContent(String.valueOf(getMax()));
            eData.appendChild(eMin);
            eData.appendChild(eMax);
            eSave.appendChild(eData);
            Element eStrategy = doc.createElement("strategy");
            cardDrawingStrategy.addStateAsXml(eStrategy);
            eSave.appendChild(eStrategy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
	 * Sets the QuizData's content according to a DOM tree (which has been loaded
	 * from the save file)
	 * The CardDrawingStrategy gets also the possibility to reload itself from the 
	 * DOM.
	 * 
	 * Note that there is no error checking. The DOM is expected to be correct.
	 *  
	 * @param state
	 */
    public void loadStateFromXml(Document state) {
        Element eData = (Element) state.getElementsByTagName("data").item(0);
        Element eMin = (Element) eData.getElementsByTagName("min").item(0);
        Element eMax = (Element) eData.getElementsByTagName("max").item(0);
        int min = Integer.parseInt(eMin.getTextContent());
        int max = Integer.parseInt(eMax.getTextContent());
        setMin(min);
        setMax(max);
        Element eStrategy = (Element) state.getElementsByTagName("strategy").item(0);
        cardDrawingStrategy.loadStateFromXml(eStrategy);
    }

    /**
	 * Reads the strategy.properties file and sets the specified class
	 * as CardDrawingStrategy.
	 * 
	 * Note that there is currently no error recovery. Valid entries are expected.
	 * 
	 * @return
	 */
    private CardDrawingStrategy getStrategyFromProperties() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("strategy.properties"));
            String classString = (String) props.get("strategy");
            CardDrawingStrategy strat = (CardDrawingStrategy) Class.forName(classString).newInstance();
            System.out.println("loaded " + strat.getClass().toString());
            return strat;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new LeitnerStrategyEqualSizedGroups();
    }

    public void initialize() {
        setMin(MIN_INDEX);
        setMax(MAX_INDEX);
        cardDrawingStrategy.initialize(MIN_INDEX, MAX_INDEX);
    }
}
