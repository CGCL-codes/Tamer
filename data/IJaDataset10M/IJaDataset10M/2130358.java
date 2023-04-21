package ch.bgaechter.wtlw;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class DataAccess {

    private ArrayList<String> quizes = new ArrayList<String>();

    private ArrayList<String> questions = new ArrayList<String>();

    private ArrayList<String> answers = new ArrayList<String>();

    public ArrayList<String> getQuizes() {
        return quizes;
    }

    public void setQuizes(ArrayList<String> quizes) {
        this.quizes = quizes;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public void readQuizes() {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build("src/quizes.xml");
            Element root = doc.getRootElement();
            List<?> allChildren = root.getChildren();
            for (int i = 0; i < allChildren.size(); i++) {
                Element quiz = (Element) allChildren.get(i);
                quizes.add(quiz.getChild("name").getText());
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readQuestions(String quizName) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc;
            doc = builder.build("src/" + quizName + ".xml");
            Element root = doc.getRootElement();
            List<?> allChildren = root.getChildren();
            for (int i = 0; i < allChildren.size(); i++) {
                Element question = (Element) allChildren.get(i);
                questions.add(question.getChild("quest").getText());
                answers.add(question.getChild("answer").getText());
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createNewQuiz(String quizName) {
        Document doc;
        try {
            SAXBuilder builder = new SAXBuilder();
            doc = builder.build("src/quizes.xml");
            Element root = doc.getRootElement();
            Element quiz = new Element("quiz");
            Element name = new Element("name");
            name.setText(quizName);
            quiz.addContent(name);
            root.addContent(quiz);
            try {
                FileOutputStream out = new FileOutputStream("src/quizes.xml");
                XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
                serializer.output(doc, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeQuestions(quizName);
    }

    private void writeQuestions(String quizName) {
        Element root = new Element("questions");
        for (int i = 0; i < answers.size(); i++) {
            Element question = new Element("question");
            Element quest = new Element("quest");
            Element answer = new Element("answer");
            quest.setText(this.questions.get(i));
            answer.setText(this.answers.get(i));
            question.addContent(quest);
            question.addContent(answer);
            root.addContent(question);
        }
        Document newDoc = new Document(root);
        try {
            FileOutputStream out = new FileOutputStream("src/" + quizName + ".xml");
            XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
            serializer.output(newDoc, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteQuiz(String quizName) {
        Document doc;
        try {
            SAXBuilder builder = new SAXBuilder();
            doc = builder.build("src/quizes.xml");
            Element root = doc.getRootElement();
            List<?> allChildren = root.getChildren();
            for (int i = 0; i < allChildren.size(); i++) {
                Element quiz = (Element) allChildren.get(i);
                if (quiz.getChild("name").getText().equals(quizName)) {
                    System.out.println("Deleting: " + quiz.getChild("name").getText());
                    root.removeContent(quiz);
                }
            }
            try {
                FileOutputStream out = new FileOutputStream("src/quizes.xml");
                XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
                serializer.output(doc, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeQuestions(quizName);
    }
}
