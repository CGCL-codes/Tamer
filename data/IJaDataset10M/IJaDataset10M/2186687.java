package ru.mail.teodorgig.mididermi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.util.Scanner;
import java.util.Vector;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * DE population class.
 * 
 * @author Todor Balabanov
 */
public class Population implements Cloneable, Serializable {

    /**
	 * Min score possible.
	 */
    private static int MIN_SCORE = 0;

    /**
	 * Max score possible.
	 */
    private static int MAX_SCORE = 9;

    /**
	 * Size of the DE population.
	 */
    private int size = 0;

    /**
	 * Chromosomes set of DE.
	 */
    private Vector<Melody> offspring;

    /**
	 * Constructor without paramteters.
	 */
    public Population() {
        super();
        offspring = new Vector<Melody>();
    }

    /**
	 * All population melodies getter.
	 * 
	 * @return All melodies.
	 */
    public Vector<Melody> getMelodies() {
        Vector<Melody> melodies = new Vector<Melody>();
        for (int i = 0; i < offspring.size(); i++) melodies.add((Melody) (offspring.elementAt(i)).clone());
        return (melodies);
    }

    /**
	 * Create random population.
	 */
    public void setRandom() {
        setRandom(size);
    }

    /**
	 * Create random population.
	 * 
	 * @param size
	 *            Size of the population.
	 */
    public void setRandom(int size) {
        Melody melody = null;
        for (int i = 0; i < size; i++) {
            melody = new Melody();
            melody.setRandom();
            melody.setId(offspring.size());
            offspring.add(melody);
        }
        this.size = offspring.size();
    }

    /**
	 * Add melody.
	 */
    public void add(Melody melody) {
        melody.setId(offspring.size());
        offspring.add(melody);
        size = offspring.size();
    }

    /**
	 * Recombination of the chromosomes according to DE rules.
	 */
    public void recombine() {
        Melody melody = null;
        int size = offspring.size();
        for (int i = 0; i < size; i++) {
            melody = (Melody) (offspring.elementAt(i)).clone();
            melody.update((offspring.elementAt(i)).getDiffertial(offspring.elementAt((int) (Math.random() * size))));
            melody.setId(offspring.size());
            offspring.add(melody);
        }
    }

    /**
	 * Determine the fitness function for each chromosome by creating a MIDI
	 * file and evaluating from the user.
	 */
    public void evaluate() {
        Scanner in = new Scanner(System.in);
        Melody melody = null;
        for (int j = 0; j < offspring.size(); j++) {
            melody = offspring.elementAt(j);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bao);
            byte[] bytes = null;
            try {
                char[] chars = melody.toBytes();
                for (int i = 0; i < chars.length; i++) {
                    out.write(chars[i]);
                }
                out.flush();
                bytes = bao.toByteArray();
                bao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
            try {
                Sequence sequence = MidiSystem.getSequence(new DataInputStream(bai));
                Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();
                sequencer.setSequence(sequence);
                sequencer.start();
            } catch (Exception ex) {
            }
            System.out.print("Score for [ " + melody.getId() + " ] : ");
            int score = MIN_SCORE;
            try {
                score = in.nextInt();
                if (score < MIN_SCORE) score = MIN_SCORE;
                if (score > MAX_SCORE) score = MAX_SCORE;
            } catch (Exception ex) {
            }
            System.out.println();
            melody.setScore(melody.getScore() + score);
        }
    }

    /**
	 * Sort the population according to the fitness value.
	 */
    public void sort() {
        Melody a, b;
        boolean done = false;
        while (done == false) {
            done = true;
            for (int i = 0; i < offspring.size() - 1; i++) {
                a = offspring.elementAt(i);
                b = offspring.elementAt(i + 1);
                if (a.getScore() < b.getScore()) {
                    offspring.removeElementAt(i);
                    offspring.insertElementAt(b, i);
                    offspring.removeElementAt(i + 1);
                    offspring.insertElementAt(a, i + 1);
                    done = false;
                }
            }
        }
    }

    /**
	 * Remove extra population chromosomes.
	 */
    public void shrink() {
        for (int i = offspring.size() - 1; i >= size; i--) offspring.removeElementAt(i);
    }

    /**
	 * Population epoches.
	 * 
	 * @param number
	 *            How many epoches.
	 */
    public void epoches(int number) {
        for (int i = 0; i < number; i++) {
            recombine();
            evaluate();
            sort();
            shrink();
        }
    }

    /**
	 * Clone the population object.
	 */
    public Object clone() {
        Population population = new Population();
        population.offspring = new Vector<Melody>();
        for (int i = 0; i < offspring.size(); i++) population.offspring.add((Melody) (offspring.elementAt(i)).clone());
        return (population);
    }
}
