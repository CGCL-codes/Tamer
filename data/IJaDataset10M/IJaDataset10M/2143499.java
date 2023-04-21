package questionary;

import myExceptions.MyFileException;

/**
 * 
 * Clase que contiene la respusta de las preguntas de tipo 5, hereda los metodos 
 * y la informacion de la clase Question
 * @see Questionary
 */
public class TrueFalseQ extends Question {

    private boolean answer;

    /**
	 * Construye una pregunta de tipo 5
	 * @param question La pregunta
	 * @param answer La respuesta
	 * @param diffic La dificultad
	 * @param score El puntaje
	 */
    public TrueFalseQ(String question, String answer, int diffic, int score) {
        super(5, diffic, score, question);
        if (answer.equals("verdadero")) this.answer = true; else this.answer = false;
    }

    @Override
    public int getOptionsCount() {
        return 2;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public boolean isAnswer() {
        return answer;
    }

    @Override
    public String[] getOptions() {
        String[] ret = { "verdadero", "falso" };
        return ret;
    }

    @Override
    public boolean isCorrect(String[] s) {
        if (s == null) return false;
        if (s[0] == null) return false;
        if (answer) return s[0].equals("verdadero");
        return s[0].equals("falso");
    }

    @Override
    public boolean isCorrect(String s) {
        if (s == null) return false;
        if (answer) return s.equals("verdadero");
        return s.equals("falso");
    }

    @Override
    public boolean isCorrect(Number n) {
        return false;
    }

    @Override
    public String[] getAnswers() {
        String[] ret = new String[1];
        if (answer) ret[0] = "verdadero"; else ret[0] = "falso";
        return ret;
    }

    @Override
    public int getCorrectOptionsCount() {
        return 1;
    }

    @Override
    public void setOptions(String[] array) {
        return;
    }

    @Override
    public void setAnswers(String[] array) throws MyFileException {
        if (array[0].equals("verdadero")) answer = true; else answer = false;
    }

    @Override
    public boolean isCorrect(String[] s, int answsize) {
        return false;
    }
}
