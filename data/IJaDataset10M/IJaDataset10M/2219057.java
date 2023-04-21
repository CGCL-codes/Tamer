package acide.gui.menuBar.editMenu.utils;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * ACIDE - A Configurable IDE search engine.
 * 
 * @version 0.8
 */
public class AcideSearchEngine {

    /**
	 * ACIDE - A Configurable IDE search engine unique class instance.
	 */
    private static AcideSearchEngine _instance;

    /**
	 * Pattern for regular expressions sequences.
	 */
    private Pattern _pattern;

    /**
	 * Matcher for the regular expressions sequences.
	 */
    private Matcher _matcher;

    /**
	 * Regular expression to search for.
	 */
    private String _regularExpresion;

    /**
	 * Counter of coincidences.
	 */
    private int _counter;

    /**
	 * Temporal position to use to fetch the search.
	 */
    private int _temporalPosition;

    /**
	 * Regular expression list to search for.
	 */
    private ArrayList<String> _regularExpresionList;

    /**
	 * Flag that indicates if the search is cyclic or not.
	 */
    private static boolean _isCycle;

    /**
	 * Returns the ACIDE - A Configurable IDE search engine unique class
	 * instance.
	 * 
	 * @return the ACIDE - A Configurable IDE search engine unique class
	 *         instance.
	 */
    public static AcideSearchEngine getInstance() {
        if (_instance == null) _instance = new AcideSearchEngine();
        return _instance;
    }

    /**
	 * Creates a new ACIDE - A Configurable IDE search engine.
	 */
    public AcideSearchEngine() {
        _regularExpresion = " ";
        _regularExpresionList = new ArrayList<String>();
        _counter = 0;
        _temporalPosition = -2;
        _isCycle = false;
    }

    /**
	 * Searches the wanted string in the text a string.
	 * 
	 * @param currentCaretPosition
	 *            current caret position.
	 * @param wantedString
	 *            wanted string.
	 * @param text
	 *            text in which the wanted string is searched for.
	 * @param isCaseSensitive
	 *            is case sensitive flag.
	 * @param isRegularExpresion
	 *            is regular expression flag.
	 * @param isCompleted
	 *            is completed flag.
	 * @param direction
	 *            search direction.
	 * 
	 * @return the position of the found coincidence.
	 */
    public int search(int currentCaretPosition, String wantedString, String text, boolean isCaseSensitive, boolean isRegularExpresion, boolean isCompleted, AcideSearchDirection direction) {
        if (isRegularExpresion) {
            if (!isCaseSensitive) _pattern = Pattern.compile(wantedString, Pattern.CASE_INSENSITIVE); else _pattern = Pattern.compile(wantedString);
            _matcher = _pattern.matcher(text);
            _regularExpresion = " ";
            switch(direction) {
                case FORWARD:
                case BOTH:
                    if (_matcher.find(currentCaretPosition)) {
                        _regularExpresion = " ";
                        _regularExpresion = _matcher.group(0).toString();
                    }
                    break;
                case BACKWARD:
                    int index = 0;
                    int limit = currentCaretPosition;
                    boolean end = false;
                    _regularExpresionList.clear();
                    while ((_matcher.find()) && (!end)) {
                        _regularExpresionList.add(_matcher.group(0).toString());
                        index = text.indexOf(_matcher.group(0).toString(), index) + _matcher.group(0).toString().length();
                        if (index > limit) {
                            end = true;
                            _regularExpresionList.remove(_regularExpresionList.size() - 1);
                        }
                    }
                    break;
            }
        }
        switch(direction) {
            case FORWARD:
                return forwardSearch(currentCaretPosition, wantedString, text, isCaseSensitive, isRegularExpresion, isCompleted, direction);
            case BACKWARD:
                return backwardSearch(currentCaretPosition, wantedString, text, isCaseSensitive, isRegularExpresion, isCompleted, direction);
            case BOTH:
                return bothSearch(currentCaretPosition, wantedString, text, isCaseSensitive, isRegularExpresion, isCompleted, direction);
        }
        return -1;
    }

    /**
	 * Searches the wanted string in the text in both directions.
	 * 
	 * @param currentCarerPosition
	 *            current caret position.
	 * @param wantedString
	 *            wanted string.
	 * @param text
	 *            text in which the wanted string is searched for.
	 * @param isCaseSensitive
	 *            is case sensitive flag.
	 * @param isRegularExpresion
	 *            is regular expression flag.
	 * @param isCompleted
	 *            is completed flag.
	 * @param direction
	 *            search direction.
	 * 
	 * @return the position of the found coincidence. If does not find any,
	 *         returns -1.
	 */
    public int bothSearch(int currentCaretPosition, String wantedString, String text, boolean isCaseSensitive, boolean isRegularExpresion, boolean isCompleted, AcideSearchDirection direction) {
        int position = -1;
        if (isCaseSensitive) {
            if ((!isRegularExpresion) && (!isCompleted)) position = text.indexOf(wantedString, currentCaretPosition);
            if (isRegularExpresion) {
                position = text.indexOf(_regularExpresion, currentCaretPosition);
            } else if (isCompleted) {
                int posi = text.indexOf(wantedString, currentCaretPosition);
                if (posi != -1) {
                    int pos2 = 0;
                    int pos1 = 0;
                    if (posi - 1 >= 0) pos1 = posi - 1; else pos1 = posi;
                    if (posi + wantedString.length() < text.length()) pos2 = posi + wantedString.length(); else pos2 = posi + wantedString.length() - 1;
                    char c1 = text.charAt(pos1);
                    char c2 = text.charAt(pos2);
                    char c = wantedString.charAt(wantedString.length() - 1);
                    if (((c1 == '\n') || (c1 >= ' ') && (c1 <= '/') || (c1 >= ':') && (c1 <= '@') || (c1 >= '[') && (c1 <= '`') || (c1 >= '{') && (c1 <= '~') || (c1 == wantedString.charAt(0))) && ((c2 == '\n') || (c2 >= ' ') && (c2 <= '/') || (c2 >= ':') && (c2 <= '@') || (c2 >= '[') && (c2 <= '`') || (c2 >= '{') && (c2 <= '~') || (c2 == c))) {
                        position = text.indexOf(wantedString, currentCaretPosition);
                    } else position = search(currentCaretPosition + 1, wantedString, text, isCaseSensitive, isRegularExpresion, isCompleted, direction);
                } else position = -1;
            }
        }
        if (!isCaseSensitive) {
            String cad2 = text.toLowerCase();
            String cad1 = wantedString.toLowerCase();
            if ((!isRegularExpresion) && (!isCompleted)) position = cad2.indexOf(cad1, currentCaretPosition);
            if (isRegularExpresion) {
                position = text.indexOf(_regularExpresion, currentCaretPosition);
            } else if (isCompleted) {
                int posi = text.indexOf(cad1, currentCaretPosition);
                if (posi != -1) {
                    int pos2 = 0;
                    int pos1 = 0;
                    if (posi - 1 >= 0) pos1 = posi - 1; else pos1 = posi;
                    if (posi + wantedString.length() < cad2.length()) pos2 = posi + cad1.length(); else pos2 = posi + cad1.length() - 1;
                    char c1 = cad2.charAt(pos1);
                    char c2 = cad2.charAt(pos2);
                    char c = cad1.charAt(cad1.length() - 1);
                    if (((c1 == '\n') || (c1 >= ' ') && (c1 <= '/') || (c1 >= ':') && (c1 <= '@') || (c1 >= '[') && (c1 <= '`') || (c1 >= '{') && (c1 <= '~') || (c1 == wantedString.charAt(0))) && ((c2 == '\n') || (c2 >= ' ') && (c2 <= '/') || (c2 >= ':') && (c2 <= '@') || (c2 >= '[') && (c2 <= '`') || (c2 >= '{') && (c2 <= '~') || (c2 == c))) {
                        position = cad2.indexOf(cad1, currentCaretPosition);
                    } else position = search(currentCaretPosition + 1, wantedString, text, isCaseSensitive, isRegularExpresion, isCompleted, direction);
                } else position = -1;
            }
        }
        if ((!_isCycle) && (_temporalPosition == -2)) _temporalPosition = currentCaretPosition;
        if ((position == -1) && (!_isCycle)) {
            _isCycle = true;
            position = search(0, wantedString, text, isCaseSensitive, isRegularExpresion, isCompleted, direction);
        } else {
            if ((_isCycle) && (_temporalPosition <= position)) return -1;
        }
        return position;
    }

    /**
	 * Searches for the wanted string in the text backwards from the current
	 * caret position.
	 * 
	 * @param currentCarerPosition
	 *            current caret position.
	 * @param wantedString
	 *            wanted string.
	 * @param text
	 *            text in which the wanted string is searched for.
	 * @param isCaseSensitive
	 *            is case sensitive flag.
	 * @param isRegularExpresion
	 *            is regular expression flag.
	 * @param isCompleted
	 *            is completed flag.
	 * @param direction
	 *            search direction.
	 * 
	 * @return the position of the found coincidence. If does not find any,
	 *         returns -1.
	 */
    public int backwardSearch(int currentCaretPosition, String wantedString, String text, boolean isCaseSensitive, boolean isRegularExpresion, boolean isCompleted, AcideSearchDirection direction) {
        int position = -1;
        if (isCaseSensitive) {
            if ((!isRegularExpresion) && (!isCompleted)) position = text.lastIndexOf(wantedString, currentCaretPosition - 1);
            if (isRegularExpresion) {
                _counter = _regularExpresionList.size() - 1;
                if (_regularExpresionList.size() > 0) {
                    _regularExpresion = _regularExpresionList.get(_counter).toString();
                    position = text.lastIndexOf(_regularExpresion, currentCaretPosition);
                } else position = -1;
            } else if (isCompleted) {
                int posi = text.lastIndexOf(wantedString, currentCaretPosition - 1);
                if (posi != -1) {
                    int pos2 = 0;
                    int pos1 = 0;
                    if (posi - 1 >= 0) pos1 = posi - 1; else pos1 = posi;
                    if (posi + wantedString.length() < text.length()) pos2 = posi + wantedString.length(); else pos2 = posi + wantedString.length() - 1;
                    char c1 = text.charAt(pos1);
                    char c2 = text.charAt(pos2);
                    char c = wantedString.charAt(wantedString.length() - 1);
                    if (((c1 == '\n') || (c1 >= ' ') && (c1 <= '/') || (c1 >= ':') && (c1 <= '@') || (c1 >= '[') && (c1 <= '`') || (c1 >= '{') && (c1 <= '~') || (c1 == wantedString.charAt(0))) && ((c2 == '\n') || (c2 >= ' ') && (c2 <= '/') || (c2 >= ':') && (c2 <= '@') || (c2 >= '[') && (c2 <= '`') || (c2 >= '{') && (c2 <= '~') || (c2 == c))) {
                        position = text.lastIndexOf(wantedString, currentCaretPosition - 1);
                    } else position = search(currentCaretPosition - 2, wantedString, text, isCaseSensitive, isRegularExpresion, isCompleted, direction);
                } else position = -1;
            }
        }
        if (!isCaseSensitive) {
            String cad2 = text.toLowerCase();
            String cad1 = wantedString.toLowerCase();
            if ((!isRegularExpresion) && (!isCompleted)) position = cad2.lastIndexOf(cad1, currentCaretPosition - 1);
            if (isRegularExpresion) {
                if (_regularExpresionList.size() > 0) {
                    _regularExpresion = _regularExpresionList.get(_regularExpresionList.size() - 1).toString();
                    position = text.lastIndexOf(_regularExpresion, currentCaretPosition - 1);
                } else position = -1;
            } else if (isCompleted) {
                int posi = cad2.lastIndexOf(cad1, currentCaretPosition - 1);
                if (posi != -1) {
                    int pos2 = 0;
                    int pos1 = 0;
                    if (posi - 1 >= 0) pos1 = posi - 1; else pos1 = posi;
                    if (posi + cad1.length() < cad2.length()) pos2 = posi + cad1.length(); else pos2 = posi + cad1.length() - 1;
                    char c1 = cad2.charAt(pos1);
                    char c2 = cad2.charAt(pos2);
                    char c = cad1.charAt(cad1.length() - 1);
                    if (((c1 == '\n') || (c1 >= ' ') && (c1 <= '/') || (c1 >= ':') && (c1 <= '@') || (c1 >= '[') && (c1 <= '`') || (c1 >= '{') && (c1 <= '~') || (c1 == cad1.charAt(0))) && ((c2 == '\n') || (c2 >= ' ') && (c2 <= '/') || (c2 >= ':') && (c2 <= '@') || (c2 >= '[') && (c2 <= '`') || (c2 >= '{') && (c2 <= '~') || (c2 == c))) {
                        position = cad2.lastIndexOf(cad1, currentCaretPosition - 1);
                    } else position = search(currentCaretPosition - 1, wantedString, text, isCaseSensitive, isRegularExpresion, isCompleted, direction);
                } else position = -1;
            }
        }
        return position;
    }

    /**
	 * Search for the wanted string in the text forwards from the current caret
	 * position.
	 * 
	 * @param currentCarerPosition
	 *            current caret position.
	 * @param wantedString
	 *            wanted string.
	 * @param text
	 *            text in which the wanted string is searched for.
	 * @param isCaseSensitive
	 *            is case sensitive flag.
	 * @param isRegularExpresion
	 *            is regular expression flag.
	 * @param isCompleted
	 *            is completed flag.
	 * @param direction
	 *            search direction.
	 * 
	 * @return the position of the found coincidence. If does not find any,
	 *         returns -1.
	 */
    public int forwardSearch(int currentCaretPosition, String wantedString, String text, boolean isCaseSensitive, boolean isRegularExpresion, boolean isCompleted, AcideSearchDirection direction) {
        int position = -1;
        if (isCaseSensitive) {
            if ((!isRegularExpresion) && (!isCompleted)) position = text.indexOf(wantedString, currentCaretPosition);
            if (isRegularExpresion) {
                if (_regularExpresion != " ") position = text.indexOf(_regularExpresion, currentCaretPosition); else position = -1;
            } else if (isCompleted) {
                int posi = text.indexOf(wantedString, currentCaretPosition);
                if (posi != -1) {
                    int pos2 = 0;
                    int pos1 = 0;
                    if (posi - 1 >= 0) pos1 = posi - 1; else pos1 = posi;
                    if (posi + wantedString.length() < text.length()) pos2 = posi + wantedString.length(); else pos2 = posi + wantedString.length() - 1;
                    char c1 = text.charAt(pos1);
                    char c2 = text.charAt(pos2);
                    char c = wantedString.charAt(wantedString.length() - 1);
                    if (((c1 == '\n') || (c1 >= ' ') && (c1 <= '/') || (c1 >= ':') && (c1 <= '@') || (c1 >= '[') && (c1 <= '`') || (c1 >= '{') && (c1 <= '~') || (c1 == wantedString.charAt(0))) && ((c2 == '\n') || (c2 >= ' ') && (c2 <= '/') || (c2 >= ':') && (c2 <= '@') || (c2 >= '[') && (c2 <= '`') || (c2 >= '{') && (c2 <= '~') || (c2 == c))) {
                        position = text.indexOf(wantedString, currentCaretPosition);
                    } else position = search(currentCaretPosition + 1, wantedString, text, isCaseSensitive, isRegularExpresion, isCompleted, direction);
                } else position = -1;
            }
        }
        if (!isCaseSensitive) {
            String cad2 = text.toLowerCase();
            String cad1 = wantedString.toLowerCase();
            if ((!isRegularExpresion) && (!isCompleted)) position = cad2.indexOf(cad1, currentCaretPosition);
            if (isRegularExpresion) {
                if (_regularExpresion != " ") position = text.indexOf(_regularExpresion, currentCaretPosition); else position = -1;
            } else if (isCompleted) {
                int posi = text.indexOf(cad1, currentCaretPosition);
                if (posi != -1) {
                    int pos2 = 0;
                    int pos1 = 0;
                    if (posi - 1 >= 0) pos1 = posi - 1; else pos1 = posi;
                    if (posi + wantedString.length() < cad2.length()) pos2 = posi + cad1.length(); else pos2 = posi + cad1.length() - 1;
                    System.out.println(pos1 + " " + pos2);
                    char c1 = cad2.charAt(pos1);
                    char c2 = cad2.charAt(pos2);
                    char c = cad1.charAt(cad1.length() - 1);
                    if (((c1 == '\n') || (c1 >= ' ') && (c1 <= '/') || (c1 >= ':') && (c1 <= '@') || (c1 >= '[') && (c1 <= '`') || (c1 >= '{') && (c1 <= '~') || (c1 == wantedString.charAt(0))) && ((c2 == '\n') || (c2 >= ' ') && (c2 <= '/') || (c2 >= ':') && (c2 <= '@') || (c2 >= '[') && (c2 <= '`') || (c2 >= '{') && (c2 <= '~') || (c2 == c))) {
                        position = cad2.indexOf(cad1, currentCaretPosition);
                    } else position = search(currentCaretPosition + 1, wantedString, text, isCaseSensitive, isRegularExpresion, isCompleted, direction);
                } else position = -1;
            }
        }
        return position;
    }

    /**
	 * Returns the regular expression to search for.
	 * 
	 * @return the regular expression to search for.
	 */
    public String getRegularExpresion() {
        return _regularExpresion;
    }

    /**
	 * Returns a regular expression from the list at the position given as a
	 * parameter.
	 * 
	 * @param position
	 *            position to get.
	 * @return a regular expression from the list parsed to string.
	 */
    public String getRegularExpresionAt(int position) {
        return _regularExpresionList.get(position).toString();
    }

    /**
	 * Returns the is cycle flag.
	 * 
	 * @return the is cycle flag.
	 */
    public boolean getIsCycle() {
        return _isCycle;
    }

    /**
	 * Sets a new value to the is cycle flag.
	 * 
	 * @param isCycle
	 *            new value to set.
	 */
    public void setIsCycle(boolean isCycle) {
        _isCycle = isCycle;
    }

    /**
	 * Returns the temporal position.
	 * 
	 * @return the temporal position.
	 */
    public int getTemporalPosition() {
        return _temporalPosition;
    }

    /**
	 * Sets a new value to the temporal position.
	 * 
	 * @param temporalPosition
	 *            new value to set.
	 */
    public void setTemporalPosition(int temporalPosition) {
        _temporalPosition = temporalPosition;
    }
}
