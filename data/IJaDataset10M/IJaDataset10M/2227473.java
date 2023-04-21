package org.jazzteam.edu.oop.ferma;

import java.util.ArrayList;
import java.util.List;

public class Fermer {

    public Fermer() {
        System.out.println("Konstructor Fermer: Fermer sozdan!");
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
	 * ���������� ��������� (� ������� ���� ���� ������)
	 */
    private List<Ovosch> ovoschi = new ArrayList<Ovosch>();

    /**
	 * ������ ����� ����������� � ������� sdat()
	 */
    private Rynoc rynoc;

    public List<Ovosch> getOvoschi() {
        return this.ovoschi;
    }

    public void printOvoschi() {
        System.out.println("������� ������ ������ ������� " + ovoschi.toString());
    }

    public List<Ovosch> sobratOvoschi(final Class claz, final int ves, final int colOvosch) throws Exception {
        for (int i = 0; i < colOvosch; i++) {
            final Object obj = claz.newInstance();
            final Ovosch ov = (Ovosch) obj;
            ov.setVes(ves);
            ovoschi.add(ov);
        }
        return ovoschi;
    }

    /**
	 * ���������� ���������� (��������� � ����� ������� tovarOvoschi ������ Rynoc ���������� ����� �������
	 * setTovarOvoschi(ovoschi) ����� ������������ ���� ���� ovoschi)
	 */
    public void sdat() {
        rynoc.setTovarOvoschiAndRemoveSource(ovoschi);
    }

    public void setRynoc(final Rynoc rynoc) {
        this.rynoc = rynoc;
    }
}
