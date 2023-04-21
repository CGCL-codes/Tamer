package com.sd_editions.collatex.Collate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import com.sd_editions.collatex.Block.Block;
import com.sd_editions.collatex.Block.BlockStructure;
import com.sd_editions.collatex.Block.BlockStructureCascadeException;
import com.sd_editions.collatex.Block.BlockStructureListIterator;
import com.sd_editions.collatex.Block.Line;
import com.sd_editions.collatex.Block.Word;
import com.sd_editions.collatex.InputPlugin.IntInputPlugin;
import com.sd_editions.collatex.InputPlugin.SimpleInputPlugin;
import com.sd_editions.collatex.InputPlugin.StringInputPlugin;
import com.sd_editions.collatex.InputPlugin.XMLInputPlugin;

public class TextAlign {

    private static String slot = new String("");

    protected ArrayList<String> base;

    protected ArrayList<String> wit;

    private ArrayList<BlockStructure> txtOrig;

    private ArrayList<ArrayList<String>> txtAlign;

    private ArrayList<ArrayList<String>> alignInfoTable;

    public TextAlign() {
        base = Lists.newArrayList();
        wit = Lists.newArrayList();
        txtOrig = Lists.newArrayList();
        txtAlign = Lists.newArrayList();
        alignInfoTable = Lists.newArrayList();
    }

    protected BlockStructure getTxtOrigBase() throws IndexOutOfBoundsException {
        int index = 0;
        return this.txtOrig.get(index);
    }

    protected BlockStructure getTxtOrigWit(int index) throws IndexOutOfBoundsException {
        return this.txtOrig.get(index);
    }

    protected ArrayList<String> getTxtAlignBase() throws IndexOutOfBoundsException {
        int index = 0;
        return this.txtAlign.get(index);
    }

    protected ArrayList<String> getTxtAlignWit(int index) throws IndexOutOfBoundsException {
        return this.txtAlign.get(index);
    }

    public ArrayList<ArrayList<String>> getAlignInfoTable() {
        return alignInfoTable;
    }

    public ArrayList<String> getAlignInfoRow(int index) {
        return alignInfoTable.get(index);
    }

    public void addAlignInfoRow(ArrayList<String> newResult) {
        this.alignInfoTable.add(newResult);
    }

    protected ArrayList<String> getBase() {
        return base;
    }

    protected void setBase(ArrayList<String> base) {
        this.base = base;
    }

    protected ArrayList<String> getWit() {
        return wit;
    }

    protected void setWit(ArrayList<String> wit) {
        this.wit = wit;
    }

    public void addNewBase(BlockStructure base) {
        int index = 0;
        txtOrig.add(index, base);
    }

    public void addNewWit(BlockStructure next) {
        txtOrig.add(next);
    }

    public void addNewWit(int index, BlockStructure next) {
        txtOrig.add(index, next);
    }

    public BlockStructure createBlockStruct(String filetyp, Object obj) {
        BlockStructure bs = null;
        IntInputPlugin plugin = null;
        SimpleInputPlugin simplePlugin = null;
        boolean cas = true;
        if (filetyp.equalsIgnoreCase("str")) {
            plugin = new StringInputPlugin((String) obj);
        } else if (filetyp.equalsIgnoreCase("xmlfile")) {
            plugin = new XMLInputPlugin(new File((String) obj));
        } else if (filetyp.equalsIgnoreCase("txtfile")) {
            simplePlugin = new SimpleInputPlugin((String) obj);
            cas = false;
        }
        if (cas) {
            try {
                bs = plugin.readFile();
            } catch (Exception e) {
            }
        } else {
            try {
                bs = simplePlugin.readFile();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            } catch (BlockStructureCascadeException e) {
            }
        }
        return bs;
    }

    public ArrayList<String> getContentofBlock(BlockStructure bs) {
        ArrayList<String> arrL = Lists.newArrayList();
        BlockStructureListIterator<? extends Block> it = bs.listIterator();
        while (it.hasNext()) {
            Object object = it.next();
            if (object instanceof Line) {
                arrL.add(String.valueOf(((Line) object).getLineNumber()));
            } else if (object instanceof Word) {
                arrL.add(((Word) object).getContent());
            }
        }
        return arrL;
    }

    public void makeSlot(int index, String name) {
        if (name.equals("base")) {
            this.base.add(index, TextAlign.slot);
        } else {
            this.wit.add(index, TextAlign.slot);
        }
    }

    public boolean proofCollate(int i, int j) {
        return !(i > ((this.base.size()) - 1) || j > ((this.wit.size()) - 1));
    }

    public void base2Slot() {
        int j = 1;
        try {
            for (int i = 1; i < this.base.size(); i++) {
                makeSlot(j, "base");
                j += 2;
            }
        } catch (Exception e) {
        }
    }

    public void wit2Slot() {
        int j = 1;
        try {
            for (int i = 1; i < this.wit.size(); i++) {
                makeSlot(j, "wit");
                j += 2;
            }
        } catch (Exception e) {
        }
    }

    public ArrayList<String> collateBase2Wit() {
        ArrayList<String> arrLnew = Lists.newArrayList();
        StringBuffer sb = new StringBuffer();
        boolean collate = true;
        boolean muneq = false;
        boolean merkh = false;
        boolean first = true;
        int pos1eq = 0;
        int pos2eq = 0;
        int posfirstuneq = 0;
        int i = 2;
        int j = 2;
        int z = 1;
        arrLnew.add(this.wit.get(0));
        while (collate) {
            if (this.base.size() == i) {
                for (int k = 0; k < ((i - posfirstuneq) / 2); k++) {
                    if (!(this.wit.size() == j)) {
                        arrLnew.add(this.wit.get(j));
                        arrLnew.add(TextAlign.slot);
                        arrLnew.add(TextAlign.slot);
                        j += 2;
                    } else {
                        arrLnew.add(TextAlign.slot);
                        arrLnew.add(TextAlign.slot);
                    }
                }
                return arrLnew;
            }
            while ((this.base.get(i).equalsIgnoreCase(this.wit.get(j)))) {
                first = true;
                if (pos1eq == 0 || !muneq) {
                    pos1eq = i;
                } else {
                    pos2eq = i;
                }
                if (muneq) {
                    muneq = false;
                    for (int k = i; k <= j; k += 2) {
                        if (j != posfirstuneq) {
                            if (!proofCollate(i, posfirstuneq)) {
                                break;
                            }
                            sb.append(this.wit.get(posfirstuneq) + " ");
                        }
                        posfirstuneq += 2;
                        merkh = true;
                        z++;
                    }
                    if (merkh) {
                        if ((pos2eq - pos1eq) / 2 == 2) {
                            arrLnew.add(TextAlign.slot);
                            sb.append("/" + this.base.get(pos2eq - 2));
                            arrLnew.add(sb.toString().trim());
                            sb.delete(0, sb.length());
                            pos1eq = 0;
                            pos2eq = 0;
                            z = 0;
                        } else {
                            arrLnew.add(sb.toString().trim());
                            sb.delete(0, sb.length());
                            z--;
                        }
                    }
                    for (int k = 0; k <= z; k++) {
                        if (posfirstuneq > i) {
                            break;
                        }
                        arrLnew.add(TextAlign.slot);
                    }
                    z = 0;
                } else {
                    if (merkh) {
                        merkh = false;
                    } else {
                        arrLnew.add(TextAlign.slot);
                    }
                    if (z == 0 && i != arrLnew.size() && i == j) {
                        for (int k = 0; k < 4; k++) {
                            arrLnew.add(TextAlign.slot);
                        }
                    }
                    arrLnew.add(this.wit.get(j));
                    j += 2;
                    i += 2;
                }
                if (!proofCollate(i, j)) {
                    break;
                }
            }
            if (!proofCollate(i, j)) {
                break;
            }
            while (!(this.base.get(i).equalsIgnoreCase(this.wit.get(j)))) {
                if (first) {
                    muneq = true;
                    first = false;
                    posfirstuneq = j;
                }
                j += 2;
                if (!proofCollate(i, j)) {
                    i += 2;
                    if (this.wit.size() - 2 > i) {
                        j = i;
                    } else {
                        z++;
                        j = posfirstuneq;
                    }
                    break;
                }
            }
        }
        if (this.base.size() > this.wit.size()) {
            for (int k = j; k < this.wit.size(); k += 2) {
                if (k >= this.wit.size() - 1) {
                    break;
                }
                arrLnew.add(TextAlign.slot);
                arrLnew.add(this.wit.get(k));
            }
            arrLnew.add(TextAlign.slot);
        } else {
            for (int k = j; k < this.wit.size(); k += 2) {
                if (k >= this.wit.size() - 1) {
                    break;
                }
                sb.append(this.wit.get(k) + " ");
            }
            arrLnew.add(sb.toString().trim());
            sb.delete(0, sb.length());
        }
        return arrLnew;
    }

    public void toString(BlockStructure bs) {
        BlockStructureListIterator<? extends Block> listIterator = bs.listIterator();
        for (int i = 0; i < bs.getNumberOfBlocks(); i++) {
            System.out.println(listIterator.next().toString());
        }
    }
}
