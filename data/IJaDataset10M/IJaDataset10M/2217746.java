package cz.cuni.mff.simpleinferer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Simple grammar inferrer - main application class.
 * 
 * @author vektor
 */
public class Main {

    /**
   * Main program body. Interprets the first command line arguemnt as the filename
   * to infer the grammar from.
   */
    public static void main(final String[] args) {
        final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        final MyHandler myHandler = new MyHandler();
        try {
            final SAXParser parser = parserFactory.newSAXParser();
            parser.parse(new File(args[0]), myHandler);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        final List<Rule> result = new ArrayList<Rule>(myHandler.getRules());
        Collections.sort(result, new Comparator<Rule>() {

            public int compare(final Rule o1, final Rule o2) {
                final int cmp = o1.getLeft().compareTo(o2.getLeft());
                if (cmp != 0) {
                    return cmp;
                }
                return Integer.valueOf(o1.getRight().size()).compareTo(Integer.valueOf(o2.getRight().size()));
            }
        });
        for (Rule rule : result) {
            if (rule.getRight().size() > 0) {
                System.out.print(rule.getLeft() + " := ");
                for (String elem : rule.getRight()) {
                    System.out.print(elem + ", ");
                }
                System.out.println();
            }
        }
    }

    /**
   * Implementation of DefaultHandler. Overrides only startElement() and 
   * endElement().
   */
    private static class MyHandler extends DefaultHandler {

        /** Stack to hold currently open elements. */
        private final Stack<Rule> stack = new Stack<Rule>();

        /** Set of rules that have been inferred so far. */
        private final Set<Rule> rules = new HashSet<Rule>();

        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (!stack.isEmpty()) {
                stack.peek().getRight().add(qName);
            }
            stack.push(new Rule(qName));
        }

        @Override
        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            final Rule end = stack.pop();
            if (!end.getLeft().equals(qName)) {
                throw new IllegalArgumentException("unpaired element");
            }
            getRules().add(end);
        }

        public Set<Rule> getRules() {
            return rules;
        }
    }

    /**
   * Class representing one CFG rule. It has a left side represented by one 
   * string and right side represented by a list of strings.
   */
    private static class Rule {

        /** Nonterminal symbol on the left side of the rule. */
        private final String left;

        /** List of nonterminal symbols on the right side of the rule. */
        private final List<String> right = new ArrayList<String>();

        public Rule(final String left) {
            this.left = left;
        }

        public String getLeft() {
            return left;
        }

        public List<String> getRight() {
            return right;
        }

        /**
     * Two rules are equal if both their sides are equal.
     * @param obj
     * @return
     */
        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof Rule)) {
                return false;
            }
            final Rule other = (Rule) obj;
            if (!other.getLeft().equals(this.getLeft())) {
                return false;
            }
            if (other.getRight().size() != this.getRight().size()) {
                return false;
            }
            for (int i = 0; i < this.getRight().size(); i++) {
                if (!other.getRight().get(i).equals(this.getRight().get(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + (this.left != null ? this.left.hashCode() : 0);
            hash = 17 * hash + (this.right != null ? this.right.hashCode() : 0);
            return hash;
        }
    }
}
