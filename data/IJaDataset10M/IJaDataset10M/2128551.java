package mjava.gc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import mjava.compiler.MJCException;
import mjava.lib.Constructeur;
import mjava.lib.DPARAMS;
import mjava.lib.DTYPE;
import mjava.lib.INFO;
import mjava.lib.Methode;
import mjava.lib.TdC;
import mjava.lib.TdS_itf;

public abstract class AbstractMachine {

    protected abstract String getSuffixe();

    protected static String path = "";

    public void writeCode(String fname, String code) throws MJCException {
        try {
            String asmName = fname + "." + getSuffixe();
            System.err.println("Ecriture du code dans " + asmName);
            FileOutputStream fs = new FileOutputStream(asmName);
            PrintWriter pw = new PrintWriter(fs);
            pw.print(code);
            pw.close();
        } catch (FileNotFoundException e) {
            throw new MJCException(e.getMessage());
        }
    }

    public static void setPath(String f) {
        int pt = f.lastIndexOf('\\');
        if (pt != -1) {
            path = f.substring(0, pt + 1);
        }
    }

    public static String getPath() {
        return path;
    }

    public abstract void setFileName(String n);

    public abstract String getFileName();

    public abstract void setCurrentClassName(String n);

    public abstract String getCurrentClassName();

    /**
	 * Genere le code qui charge le pointeur null sur la pile
	 * @return		code assembleur correspondant
	 */
    public abstract String codeNull();

    /**
	 * Genere le code qui charge la valeur True sur la pile
	 * @return		code assembleur correspondant
	 */
    public abstract String codeTrue();

    /**
	 * Genere le code qui charge la valeur False sur la pile
	 * @return		code assembleur correspondant
	 */
    public abstract String codeFalse();

    /**
	 * Genere le code qui charge une valeur numerique sur la pile
	 * @i					entier a charger dans la pile
	 * @return				code assembleur correspondant
	 */
    public abstract String codeInt(String i);

    /**
	 * Genere le code qui permet de charger dans la pile l'adresse de this
	 * on se debrouille pour que this soit toujours � l'adresse -1[LB]
	 * @return 		code assembleur correspondant
	 */
    public abstract String codeThis();

    /**
	 * Genere le code qui permet d'enregistrer les donnees du haut de la pile
	 * dans un espace memoire variable
	 *
	 * @param reg		registre par rapport au quel on situ l'espace memoire destination
	 * @param dep		deplacement par rapport au registre
	 * @param taille	taille de donnees a affecter
	 * @return			code assembleur correspondant
	 */
    public abstract String codeAffect(String reg, int dep, int taille);

    public abstract String codeDeclVar(INFO var);

    /**
	 * Gener le code pour charger une variable dans la pile
	 * @param attr		descripteur de la variable
	 * @return			code assembleur correspondant
	 */
    public abstract String codeLoadVar(INFO i, boolean global);

    /**
	 * genere le code de negation booleene
	 * @return			code assembleur correspondant
	 */
    public abstract String codeNot();

    /**
	 * genere le code de and booleene
	 * @return			code assembleur correspondant
	 */
    public abstract String codeAnd();

    /**
	 * genere le code de or booleene
	 * @return			code assembleur correspondant
	 */
    public abstract String codeOr();

    /**
	 * genere le code de negation entiere
	 * @return			code assembleur correspondant
	 */
    public abstract String codeNegInt();

    /**
	 * genere le code pour l'addition
	 * @return			code assembleur correspondant
	 */
    public abstract String codeAddInt();

    /**
	 * genere le code pour la soustraction
	 * @return			code assembleur correspondant
	 */
    public abstract String codeMinusInt();

    /**
	 * genere le code pour la multiplication
	 * @return			code assembleur correspondant
	 */
    public abstract String codeMultInt();

    /**
	 * genere le code pour la division
	 * @return			code assembleur correspondant
	 */
    public abstract String codeDivInt();

    /**
	 * genere le code pour le reste de la division entiere
	 * @return			code assembleur correspondant
	 */
    public abstract String codeModInt();

    /**
	 * genere le code pour la comparaison  <
	 * @return			code assembleur correspondant
	 */
    public abstract String codeLessInt();

    /**
	 * genere le code pour la comparaison <=
	 * @return			code assembleur correspondant
	 */
    public abstract String codeLessEqualInt();

    /**
	 * genere le code pour la comparaison >
	 * @return			code assembleur correspondant
	 */
    public abstract String codeMoreInt();

    /**
	 * genere le code pour la comparaison >=
	 * @return			code assembleur correspondant
	 */
    public abstract String codeMoreEqualInt();

    /**
	 * genere le code pour la comparaison ==
	 * @return			code assembleur correspondant
	 */
    public abstract String codeEqualInt();

    /**
	 * genere le code pour la comparaison !=
	 * @return			code assembleur correspondant
	 */
    public abstract String codeNotEqualInt();

    /**
	 * Generation de lable
	 * @param  lable String
	 * @return	le lable
	 */
    public abstract String codeNewLable(String lable);

    /**
	 * Genere le code pour la structure tantque
	 * @param condition		chaine de caracters qui calcul la condition
	 * @param bloc		chaine de caracters qui represente le bloc de tant que 
	 * @return			code assembleur correspondant
	 */
    public abstract String codeWhile(String condition, String bloc);

    /**
	 * Generation de code pour une structure de type if ... then ... else ...
	 * @param condition		code correspondant au calcul de la condition du if
	 * @param blocTrue		code du premier bloc
	 * @param blocFalse		code du second bloc
	 * @return			code assembleur correspondant 
	 */
    public abstract String codeIf(String condition, String blocTrue, String blocFalse);

    /**
	 * Genere le code pour une declaration de variable
	 * @param v		informations sur la variable
	 * @param expr	expression qui calcul la valeur a affecter a cet variable (peut etre vide)
	 * @return		code assembleur correspondant
	 */
    public abstract String codeDeclVar(INFO var, String expr, boolean isVal);

    /**
	 * Genere le code pour le bloc {...}
	 * @param insts		le code des instructions contenues dans le bloc
	 * @param tds		tds du bloc
	 * @return			code assembleur correspondant
	 */
    public abstract String codeBloc(String insts, TdS_itf tds);

    public abstract String codeMethodDef(Methode m, String bloc);

    public abstract String codeLoadValue(int v);

    public abstract String codeLoadValue(boolean b);

    public abstract String codeConstructorDef(Constructeur c, String bloc);

    public abstract String codeMethodCall(Methode m, DPARAMS params, String args);

    public abstract String codeConstructorCall(Constructeur c, String args, String name);

    public abstract String codeLoadValFromAddr(String e, DTYPE t);

    public abstract String codeClassInfo(TdC tdc);

    public String codeSuper() {
        return "";
    }

    public abstract String codeNoAssignmentInst(String e, DTYPE t);

    public abstract String codeAssignmentInst(String n1, String n2, boolean isVal1, boolean isVal2, INFO i1, INFO i2);

    public abstract String codeReturn(String e, DTYPE t, DPARAMS p);

    public abstract String codeLoadVarAddr(INFO i);

    public void writeTdCFile(String name, TdS_itf tds) {
        TdC tdc = (TdC) tds;
        try {
            FileOutputStream fos = new FileOutputStream(path + name + ".tdc");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tdc);
            oos.close();
        } catch (IOException e) {
            System.err.println("Can not write file: " + name + ".tdc");
            e.printStackTrace();
        }
    }
}
