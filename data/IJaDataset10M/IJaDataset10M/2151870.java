package com.tabuto.jenetic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;

/**
 * Class <code> Dna</code>
 * <p>
 * <code>Dna</code> is a class that represent a simple model of DNA as a {@link Gene} Vector.
 * Add a {@link Gene} into <code>Dna</code> is simple by using the {@link Dna#add(Gene)} ,{@link Dna#add(double) }
 * {@link Dna#add(boolean)} methods.
 * So the <code>Dna</code> is just a information's object container, able to save or load itself using an XML file
 * thanks to methods {@link Dna#save(String)} and {@link Dna#load(String)}
 * <code>Dna</code> class also provides a {@link Dna#merge(Dna)} method simulating the Dna Genetic Recombination.
 * During this process it's possible introduce some "noise" using the {@link Dna#param} variable.
 *
 * @author tabuto83
 * 
 * @version 0.2.3
 * 
 * @see Gene
 */
public class Dna implements Serializable {

    /**
	 * The serial Version UID
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * The Vector Gene is a dinamic array where {@link Gene} are stored
	 */
    @SuppressWarnings("unchecked")
    protected Vector<Gene> DNA;

    /**
	 * This variable represent the percent of mutation introduced by {@link Dna#merge(Dna)} method.
	 * The {@code param} value should be between 0 and 1. Default value is 0;
	 * 
	 * @see Dna#merge(Dna)
	 * @see Dna#merge(Dna, Dna)
	 */
    protected double param;

    /**
	 * The name of this DNA
	 */
    private String Name;

    /**
	 * This method instance a new empty <code>Dna</code> Object.
	 */
    @SuppressWarnings("unchecked")
    public Dna() {
        this.DNA = new Vector<Gene>();
        this.param = 0;
        this.Name = "";
    }

    /**
	 * This method instance a new empty <code>Dna</code> Object with a parameter for Genetic Recombination.
	 * 
	 * @param p <code>double</code> Dna Parameter {@link Dna#param}
	 */
    public Dna(double p) {
        this();
        this.setParam(p);
    }

    /**
	 * This method instance a new  <code>Dna</code> Object using a specified name
	 * @param Name <code>String</code> the Dna name
	 */
    public Dna(String Name) {
        this();
        this.setName(Name);
    }

    /**
	 * This method instance a new  <code>Dna</code> Object using a specified name
	 * and {@link Dna#param}
	 * 
	 * @param Name <code>String</code> the Dna Name
	 * @param p <code>double</code> Dna {@link Dna#param}
	 */
    public Dna(String Name, double p) {
        this(p);
        this.setName(Name);
    }

    /**
	 * Append a new {@link Gene} into <code>Dna</code> Vector
	 * @param g <code>Gene</code> newGene
	 */
    @SuppressWarnings("unchecked")
    public void add(Gene g) {
        this.DNA.add(g);
    }

    /**
	 * Add a new Double {@link Gene} into <code>Dna</code>  
	 * @param d <code>double</code> Gene Double Value
	 */
    @SuppressWarnings("unchecked")
    public void add(double d) {
        this.DNA.add(new Gene(d));
    }

    /**
	 * Add a new Double {@link Gene} into <code>Dna</code> by setting his value and his name
	 * @param d <code>double</code> Gene Value
	 * @param Name <code>String</code> Gene's Name
	 */
    @SuppressWarnings("unchecked")
    public void add(double d, String Name) {
        this.DNA.add(new Gene(d, Name));
    }

    /**
	 * Add a new Double {@link Gene} into <code>Dna</code> by setting his value and his name
	 * @param d <code>double</code> Gene Value
	 * @param Name <code>String</code> Gene's Name
	 * @param description <code>String</code> Gene's Description
	 */
    @SuppressWarnings("unchecked")
    public void add(double d, String Name, String description) {
        this.DNA.add(new Gene(d, Name, description));
    }

    /**
	 * Add a new Integer {@link Gene} into <code>Dna</code>  
	 * @param d <code>Integer</code> Gene int Value
	 */
    @SuppressWarnings("unchecked")
    public void add(Integer d) {
        this.DNA.add(new Gene(d));
    }

    /**
	 * Add a new Integer {@link Gene} into <code>Dna</code> by setting his value and his name
	 * @param d <code>Integer</code> Gene Value
	 * @param Name <code>String</code> Gene's Name
	 */
    @SuppressWarnings("unchecked")
    public void add(Integer d, String Name) {
        this.DNA.add(new Gene(d, Name));
    }

    /**
	 * Add a new Integer {@link Gene} into <code>Dna</code> by setting his value and his name
	 * @param d <code>int</code> Gene Value
	 * @param Name <code>String</code> Gene's Name
	 * @param description <code>String</code> Gene's Description
	 */
    @SuppressWarnings("unchecked")
    public void add(Integer d, String Name, String description) {
        this.DNA.add(new Gene(d, Name, description));
    }

    /**
	 * Add a new Integer {@link Gene} into <code>Dna</code>  
	 * @param d <code>Integer</code> Gene int Value
	 */
    @SuppressWarnings("unchecked")
    public void add(int d) {
        this.DNA.add(new Gene(d));
    }

    /**
	 * Add a new Integer {@link Gene} into <code>Dna</code> by setting his value and his name
	 * @param d <code>Integer</code> Gene Value
	 * @param Name <code>String</code> Gene's Name
	 */
    @SuppressWarnings("unchecked")
    public void add(int d, String Name) {
        this.DNA.add(new Gene(d, Name));
    }

    /**
	 * Add a new Integer {@link Gene} into <code>Dna</code> by setting his value and his name
	 * @param d <code>int</code> Gene Value
	 * @param Name <code>String</code> Gene's Name
	 * @param description <code>String</code> Gene's Description
	 */
    @SuppressWarnings("unchecked")
    public void add(int d, String Name, String description) {
        this.DNA.add(new Gene(d, Name, description));
    }

    /**
	 * Add a new Byte {@link Gene} into <code>Dna</code>  
	 * @param d <code>Byte</code> Gene Double Value
	 */
    @SuppressWarnings("unchecked")
    public void add(Byte d) {
        this.DNA.add(new Gene(d));
    }

    /**
	 * Add a new Byte {@link Gene} into <code>Dna</code> by setting his value and his name
	 * @param d <code>Byte</code> Gene Value
	 * @param Name <code>String</code> Gene's Name
	 */
    @SuppressWarnings("unchecked")
    public void add(Byte d, String Name) {
        this.DNA.add(new Gene(d, Name));
    }

    /**
	 * Add a new Byte {@link Gene} into <code>Dna</code> by setting his value and his name
	 * @param d <code>Byte</code> Gene Value
	 * @param Name <code>String</code> Gene's Name
	 * @param description <code>String</code> Gene's Description
	 */
    @SuppressWarnings("unchecked")
    public void add(Byte d, String Name, String description) {
        this.DNA.add(new Gene(d, Name, description));
    }

    /**
	 * Add a new Boolean {@link Gene} into <code>Dna</code> 
	 * @param b <code>boolean</code> Gene Value
	 */
    @SuppressWarnings("unchecked")
    public void add(boolean b) {
        this.DNA.add(new Gene(b));
    }

    /**
	 * Add a new Boolean {@link Gene} into <code>Dna</code>  by setting his value and his name
	 * @param b <code>boolean</code> Gene Value
	 * @param Name <code>String</code> Gene's Name
	 */
    @SuppressWarnings("unchecked")
    public void add(boolean b, String Name) {
        this.DNA.add(new Gene(b, Name));
    }

    /**
	 * Add a new Boolean {@link Gene} into <code>Dna</code>  by setting his value and his name
	 * @param b <code>boolean</code> Gene Value
	 * @param Name <code>String</code> Gene's Name
	 */
    @SuppressWarnings("unchecked")
    public void add(boolean b, String Name, String Description) {
        this.DNA.add(new Gene(b, Name, Description));
    }

    /**
	 * Return the {@link Gene} at specified index
	 * @param index <code>int</code> Dna's Vector Index
	 * @return g <code> Gene </code>
	 */
    @SuppressWarnings("unchecked")
    public Gene getGene(int index) {
        return this.DNA.get(index);
    }

    /**
	 * Return the {@link Gene} with specified {@code String} name
	 * @param name the name of Gene
	 * @return Gene with specified name
	 */
    @SuppressWarnings("unchecked")
    public Gene getGene(String name) {
        return this.findGeneByName(name);
    }

    /**
	 * @return the name of this Dna
	 */
    public String getName() {
        return this.Name;
    }

    /**
	 * Return the {@link Dna#param} variable of Dna.
	 * @return <code>double</code> Dna Parameter {@link Dna#param}
	 */
    public double getParam() {
        return this.param;
    }

    /**
	 * Return the {@link Gene} double's value at specific index.
	 * @return d <code>double</code>
	 */
    public int getSize() {
        return this.DNA.size();
    }

    /**
	 * This method merge this Dna with other and return a new Dna Code.
	 * This method add some random error, if {@link Dna#param} is not equals to zero,
	 *  in the merging process in order to simulate a genetic mutation;
	 * @param otherDna
	 * @return new Dna
	 */
    public Dna merge(Dna otherDna) {
        Dna result = new Dna();
        result.setParam(this.param + otherDna.getParam() + ((int) Math.abs(Math.random() - 0.4)));
        if (this.getSize() == otherDna.getSize()) {
            for (int i = 0; i < this.getSize(); i++) {
                result.add(combineGene(this.DNA.get(i), otherDna.DNA.get(i)));
            }
        }
        return result;
    }

    /**
	 * {@code Static }method returns a new Dna as a parametric merge between two 
	 * same-size Dna. 
	 * @param firstDna 
	 * @param secondDna
	 * @return new merged Dna
	 * @see Dna#param
	 */
    public static Dna merge(Dna firstDna, Dna secondDna) {
        Dna result = new Dna();
        result.setParam(firstDna.param + secondDna.getParam() + ((int) Math.abs(Math.random() - 0.4)));
        if (firstDna.getSize() == secondDna.getSize()) {
            for (int i = 0; i < firstDna.getSize(); i++) {
                result.add(staticCombineGene(firstDna.getGene(i), secondDna.getGene(i), result.getParam()));
            }
        }
        return result;
    }

    /**
	 * Load a list of Genes from an XML file previously saved using {@link Dna#save(String)}
	 * Can't load Gene containing not primitive objects!!
	 * @param fileName <code>String</code> 
	 * @deprecated Use static method {@link Dna#load(String)}
	 * 
	 */
    @SuppressWarnings("unchecked")
    public void loadXML(String fileName) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new File(fileName));
            Element root = document.getRootElement();
            List children = root.getChildren();
            Iterator iterator = children.iterator();
            Element dna = (Element) iterator.next();
            this.setName(dna.getAttributeValue("Name"));
            this.setParam(Double.parseDouble(dna.getAttributeValue("Param")));
            while (iterator.hasNext()) {
                Element gene = (Element) iterator.next();
                Gene g = new Gene();
                if (gene.getAttributeValue("Type").equalsIgnoreCase("Bool")) g.setValue((Boolean.parseBoolean(gene.getAttributeValue("Value"))));
                if (gene.getAttributeValue("Type").equalsIgnoreCase("Double")) g.setValue((Double.parseDouble(gene.getAttributeValue("Value"))));
                if (gene.getAttributeValue("Type").equalsIgnoreCase("Integer")) g.setValue((Integer.parseInt(gene.getAttributeValue("Value"))));
                if (gene.getAttributeValue("Type").equalsIgnoreCase("Byte")) g.setValue((Byte.parseByte(gene.getAttributeValue("Value"))));
                g.setName(gene.getAttributeValue("Name"));
                g.setDescription(gene.getAttributeValue("Description"));
                this.DNA.add(g);
            }
        } catch (Exception e) {
            System.err.println("Errore durante la lettura dal file");
            e.printStackTrace();
        }
    }

    /**
	 * {@code Static }method returns a previusly saved Dna. 
	 * Dna file must be a previusly saved .dna file extension
	 * @param filename file name
	 * @return a file loaded {@link Dna}
	 * @see {@link Dna#save(String)}
	 */
    public static Dna load(String filename) {
        Dna loaded = null;
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(filename);
            in = new ObjectInputStream(fis);
            loaded = (Dna) in.readObject();
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return loaded;
    }

    /**
	 * Save a copy of DNA object in a binary file with .dna extension. It can be load using the following code:
	 *  <pre>
	 *  Dna MyNewDna = null;
	 *  FileInputStream fis = null;
	 *  ObjectInputStream in = null;
	 *  try
	 *     {
	 *     fis = new FileInputStream(filename);
	 *     in = new ObjectInputStream(fis);
	 *     MyNewDna = (Dna)in.readObject();
	 *     in.close();
	 *     }
	 *     catch(IOException ex)
	 *     {
	 *     ex.printStackTrace();
	 *     }
	 *     catch(ClassNotFoundException ex)
	 *     {
	 *     ex.printStackTrace();
	 *     }
	 *     </pre>
	 *
	 * @param filename
	 */
    public void save(String filename) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(filename + ".dna");
            out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Set the name of this Dna
	 * @param name of this Dna
	 */
    public void setName(String name) {
        this.Name = name;
    }

    /**
	 * Set the {@link Dna#param} variable of Dna. 
	 * Should be a double between 0 and 1
	 * @param p <code>double</code> Dna Parameter {@link Dna#param}
	 */
    public void setParam(double p) {
        if (p < 0) this.param = Math.abs(p);
        if (p > 1) this.param = 1; else this.param = p;
    }

    /**
	 * Set the {@link Dna#param} variable of Dna as a random double 
	 */
    public void setRandomParam() {
        this.param = Math.random();
    }

    public String toString() {
        String output = "\n DNA " + "\n Name: " + this.getName() + "\n Parameter: " + this.getParam() + "\n Size: " + this.getSize() + "\n---------------------------------" + "\n Genes List" + "\n---------------------------------";
        for (int i = 0; i < this.DNA.size(); i++) {
            output += this.getGene(i).toString() + "\n---------------------------------";
        }
        return output;
    }

    /**
	 * Return a XML document represent this DNA with description List of ist genes
	 * @see Dna#load(String)
	 */
    public Document toXML() {
        Element DnaRoot = new Element("DNA");
        Document document = new Document(DnaRoot);
        Element Dna = new Element("DNA-MetaInfo");
        Dna.setAttribute("Name", this.Name);
        Dna.setAttribute("Param", String.valueOf(this.param));
        Dna.setAttribute("Size", String.valueOf(this.getSize()));
        DnaRoot.addContent(Dna);
        for (int i = 0; i < this.DNA.size(); i++) {
            Element gene = new Element("Gene-" + i);
            gene.setAttribute("index", String.valueOf(i));
            gene.setAttribute("Name", this.getGene(i).getName());
            gene.setAttribute("Type", this.getGene(i).getType());
            gene.setAttribute("Value", this.getGene(i).stringValue());
            gene.setAttribute("Description", this.getGene(i).getDescription());
            gene.setName(gene.getName() + "-" + gene.getAttributeValue("Name"));
            DnaRoot.addContent(gene);
        }
        return document;
    }

    /**
	 * Write a XML file represent this DNA with description List of ist genes
	 * @param fileName <code>String</code> 
	 * @see Dna#load(String)
	 */
    public void toXML(String fileName) {
        try {
            Element DnaRoot = new Element("DNA");
            Document document = new Document(DnaRoot);
            Element Dna = new Element("DNA");
            Dna.setAttribute("Name", this.Name);
            Dna.setAttribute("Param", String.valueOf(this.param));
            Dna.setAttribute("Size", String.valueOf(this.getSize()));
            DnaRoot.addContent(Dna);
            for (int i = 0; i < this.DNA.size(); i++) {
                Element gene = new Element("Gene-" + i);
                gene.setAttribute("index", String.valueOf(i));
                gene.setAttribute("Name", this.getGene(i).getName());
                gene.setAttribute("Type", this.getGene(i).getType());
                gene.setAttribute("Value", this.getGene(i).stringValue());
                gene.setAttribute("Description", this.getGene(i).getDescription());
                gene.setName(gene.getName() + "-" + gene.getAttributeValue("Name"));
                DnaRoot.addContent(gene);
            }
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(document, new FileOutputStream(fileName + ".xml"));
        } catch (IOException e) {
            System.err.println("Errore durante il parsing del documento");
            e.printStackTrace();
        }
    }

    /**
	 * Return a new Gene as a parametric {@link Dna#param} combination between they values
	 */
    @SuppressWarnings("unchecked")
    protected Gene combineGene(Gene g1, Gene g2) throws NumberFormatException {
        Gene G = new Gene();
        double var = 0;
        if (g1.isMeargeable() && g2.isMeargeable()) {
            if (g1.getType().equalsIgnoreCase(g2.getType())) {
                if (g1.getType().equalsIgnoreCase("Boolean")) G.setValue(g1.booleanValue() & g2.booleanValue());
                if (g1.getType().equalsIgnoreCase("Integer") || g1.getType().equalsIgnoreCase("int")) {
                    var = ((g1.intValue() + g2.intValue())) * (0.1 + Math.random() * this.param);
                    G.setValue((int) (((g1.intValue() + g2.intValue()) / 2) + Math.random() * var * 2));
                }
                if (g1.getType().equalsIgnoreCase("Byte")) G.setValue(g1.byteValue() & g2.byteValue());
                if (g1.getType().equalsIgnoreCase("Double")) {
                    var = ((g1.doubleValue() + g2.doubleValue())) * (0.1 + Math.random() * this.param);
                    G.setValue((((g1.doubleValue() + g2.doubleValue()) / 2) + Math.random() * var * 2));
                }
                G.setName(g1.getName());
                G.setDescription(g1.getDescription());
                G.setMergeable(true);
            }
        } else {
            int random = (int) (Math.random() * 2);
            switch(random) {
                case 0:
                    G.setValue((g1.getOb()));
                    break;
                case 1:
                    G.setValue((g2.getOb()));
            }
        }
        return G;
    }

    /**
	 * Find a Dna's Gene by his Name
	 * @param name Gene's Name
	 * @return Gene
	 */
    @SuppressWarnings("unchecked")
    protected Gene findGeneByName(String name) {
        boolean find = false;
        int index = 0;
        for (int i = 0; i < this.getSize(); i++) if (this.getGene(i).getName().equalsIgnoreCase(name)) {
            find = true;
            index = i;
        }
        if (find) return this.getGene(index); else return null;
    }

    /**
	 * Return a new Gene as a parametric {@link Dna#param} combination between they values
	 */
    @SuppressWarnings("unchecked")
    private static Gene staticCombineGene(Gene g1, Gene g2, double param) throws NumberFormatException {
        Gene G = new Gene();
        if (g1.isMeargeable() && g2.isMeargeable()) {
            if (g1.getType().equalsIgnoreCase(g2.getType())) {
                if (g1.getType().equalsIgnoreCase("Boolean")) G.setValue(g1.booleanValue() & g2.booleanValue());
                double mergeResultD = ((g1.doubleValue() + g2.doubleValue()) / 2) * param;
                int mergeResultI = (int) ((int) ((g1.doubleValue() + g2.doubleValue()) / 2) * param);
                if (g1.getType().equalsIgnoreCase("Integer") || g1.getType().equalsIgnoreCase("int")) G.setValue((int) (((g1.intValue() + g2.intValue()) / 2) - mergeResultI + (int) (Math.random() * 2 * mergeResultI)));
                if (g1.getType().equalsIgnoreCase("Byte")) G.setValue(g1.byteValue() & g2.byteValue());
                if (g1.getType().equalsIgnoreCase("Double")) G.setValue((((g1.doubleValue() + g2.doubleValue()) / 2) - mergeResultD + (Math.random() * 2 * mergeResultD)));
                ;
                G.setName(g1.getName());
                G.setDescription(g1.getDescription());
                G.setMergeable(true);
            }
        } else {
            int random = (int) (Math.random() * 2);
            switch(random) {
                case 0:
                    G.setValue((g1.getOb()));
                    break;
                case 1:
                    G.setValue((g2.getOb()));
            }
        }
        return G;
    }
}
