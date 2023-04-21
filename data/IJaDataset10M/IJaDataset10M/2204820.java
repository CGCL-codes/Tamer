package boccaccio.andrea.mySimpleSynchronizer.guiFrontend.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Andrea Boccaccio
 *
 */
public class MainModel extends Observable implements IFullModelWithObserver, IFullModel, IReadOnlyModel, IModelForThread, ILastOutputSetter {

    private String strSource;

    private String strDestination;

    private int iOptions;

    private int iRegexType;

    private String strRegex;

    private boolean bComputing;

    private ProcessStarter ps;

    private StringBuilder strbldOutput;

    private String strLastOutput;

    private PrintStream psOutput;

    private String strAbout;

    private String strAboutTitle;

    public MainModel() {
        super();
        StringBuilder strBldAbout = new StringBuilder();
        this.strSource = "";
        this.strDestination = "";
        this.iOptions = 0;
        this.iRegexType = 0;
        this.strRegex = "";
        this.bComputing = false;
        this.ps = null;
        this.strbldOutput = new StringBuilder();
        this.strLastOutput = "";
        this.psOutput = new PrintStream(new MyFilteredStream(new ByteArrayOutputStream(), this));
        System.setOut(this.psOutput);
        System.setErr(this.psOutput);
        strBldAbout.append("Copyright 2009 Andrea Boccaccio");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("e-mail:	andrea_boccaccio@yahoo.it");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("phone:	+393388098951");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("JavaMySimpleSynchronizer is free software: you can redistribute it and/or modify");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("it under the terms of the GNU General Public License as published by");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("the Free Software Foundation, either version 3 of the License, or");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("(at your option) any later version.");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("JavaMySimpleSynchronizer is distributed in the hope that it will be useful,");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("but WITHOUT ANY WARRANTY; without even the implied warranty of");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("GNU General Public License for more details.");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("You should have received a copy of the GNU General Public License");
        strBldAbout.append(System.getProperty("line.separator"));
        strBldAbout.append("along with JavaMySimpleSynchronizer.  If not, see <http://www.gnu.org/licenses/>.");
        this.setStrAbout(strBldAbout.toString());
        this.setStrAboutTitle("About JavaMySimpleSynchronizer");
    }

    public String getStrSource() {
        return strSource;
    }

    public void setStrSource(String strSource) {
        if (!this.strSource.equals(strSource)) {
            this.strSource = strSource;
            this.setChanged();
            this.notifyObservers();
        }
    }

    public String getStrDestination() {
        return strDestination;
    }

    public void setStrDestination(String strDestination) {
        if (!this.strDestination.equals(strDestination)) {
            this.strDestination = strDestination;
            this.setChanged();
            this.notifyObservers();
        }
    }

    public int getIOptions() {
        return iOptions;
    }

    public void setIOptions(int options) {
        if (this.iOptions != options) {
            iOptions = options;
            this.setChanged();
            this.notifyObservers();
        }
    }

    public int getIRegexType() {
        return iRegexType;
    }

    public void setIRegexType(int regexType) {
        if (this.iRegexType != regexType) {
            iRegexType = regexType;
            this.setChanged();
            this.notifyObservers();
        }
    }

    public String getStrRegex() {
        return strRegex;
    }

    public void setStrRegex(String strRegex) {
        if (!this.strRegex.equals(strRegex)) {
            this.strRegex = strRegex;
            this.setChanged();
            this.notifyObservers();
        }
    }

    public synchronized boolean isBComputing() {
        return bComputing;
    }

    public synchronized void setBComputing(boolean computing) {
        if (this.bComputing != computing) {
            this.bComputing = computing;
            this.setChanged();
            this.notifyObservers();
        }
    }

    public synchronized StringBuilder getStrbldOutput() {
        return strbldOutput;
    }

    protected void setStrbldOutput(StringBuilder strbldOutput) {
        this.strbldOutput = strbldOutput;
    }

    public String getStrLastOutput() {
        return strLastOutput;
    }

    public synchronized void setStrLastOutput(String strLastOutput) {
        this.strLastOutput = strLastOutput;
        this.getStrbldOutput().append(strLastOutput);
        this.setChanged();
        this.notifyObservers();
    }

    /**
	 * @return the strAbout
	 */
    public String getStrAbout() {
        return strAbout;
    }

    /**
	 * @param strAbout the strAbout to set
	 */
    private void setStrAbout(String strAbout) {
        this.strAbout = strAbout;
    }

    /**
	 * @return the strAboutTitle
	 */
    public String getStrAboutTitle() {
        return strAboutTitle;
    }

    /**
	 * @param strAboutTitle the strAboutTitle to set
	 */
    private void setStrAboutTitle(String strAboutTitle) {
        this.strAboutTitle = strAboutTitle;
    }

    public synchronized String getCommand() {
        StringBuilder strbldRet = new StringBuilder();
        String[] strArrayTemp = this.getCommandStrArray();
        int i;
        for (i = 0; i < (strArrayTemp.length - 2); ++i) {
            if (i > 0) strbldRet.append(" ");
            strbldRet.append(strArrayTemp[i]);
        }
        for (; i < strArrayTemp.length; ++i) {
            if (i > 0) strbldRet.append(" ");
            strbldRet.append(this.checkPath(strArrayTemp[i]));
        }
        return strbldRet.toString();
    }

    public synchronized String[] getCommandStrArray() {
        String[] strArrayRet;
        List<String> lsTemp = new ArrayList<String>();
        StringBuilder strbldTemp = new StringBuilder();
        String[] strOptions = { "", "-fs", "-fd", "-csic", "-cdic", "-cs", "-cd", "-ms", "-md" };
        String[] strRegex = { "", "-ir", "-mr" };
        lsTemp.add("java");
        lsTemp.add("-cp");
        strbldTemp.append("*");
        strbldTemp.append(System.getProperty("path.separator"));
        strbldTemp.append("lib");
        strbldTemp.append(File.separator);
        strbldTemp.append("*");
        lsTemp.add(strbldTemp.toString());
        strbldTemp.delete(0, strbldTemp.length());
        lsTemp.add("boccaccio.andrea.mySimpleSynchronizer.MySimpleSynchronizer");
        if (this.getIOptions() > 0) {
            lsTemp.add(strOptions[this.getIOptions()]);
        }
        if (this.getIRegexType() > 0) {
            lsTemp.add(strRegex[this.getIRegexType()]);
            lsTemp.add(this.getStrRegex());
        }
        lsTemp.add(this.getStrSource());
        lsTemp.add(this.getStrDestination());
        strArrayRet = new String[lsTemp.size()];
        for (int i = 0; i < lsTemp.size(); ++i) {
            strArrayRet[i] = lsTemp.get(i);
        }
        return strArrayRet;
    }

    public void startSync() {
        ps = new ProcessStarter(this);
        ps.start();
    }

    public void stop() {
        if ((this.isBComputing()) && (this.ps != null)) {
            ps.interrupt();
        }
    }

    public void exit() {
        if ((this.isBComputing()) && (this.ps != null)) {
            this.stop();
        }
        System.exit(0);
    }

    @Override
    public synchronized void addObserver(Observer arg0) {
        super.addObserver(arg0);
        this.setChanged();
        this.notifyObservers();
    }

    private String checkPath(String strPath) {
        StringBuilder strbldRit = new StringBuilder();
        strbldRit.append(strPath);
        if (strPath.indexOf(" ") >= 0) {
            strbldRit.insert(0, "\"");
            strbldRit.append("\"");
        }
        return strbldRit.toString();
    }
}
