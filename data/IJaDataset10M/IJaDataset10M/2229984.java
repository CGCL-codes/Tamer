package org.lxb.smallgame;

/**
 *���ӳ���
 * @author luxinbi
 */
public class Qizi extends javax.swing.JButton {

    private int xx;

    private int yy;

    public int getRole() {
        return role;
    }

    public int getXx() {
        return xx;
    }

    public void setXx(int xx) {
        this.xx = xx;
    }

    public int getYy() {
        return yy;
    }

    public void setYy(int yy) {
        this.yy = yy;
    }

    /**
     * 1��0��
     */
    private int role;

    public Qizi(int role) {
        super();
        this.role = role;
        this.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
    }

    public void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        MainFrame a = Main.getMain();
        if (a.getCurole() == role && !this.equals(a.getCumove())) {
            a.setCumove(this);
            System.out.println("qiziX:" + this.getX() / 100 + ", Y:" + this.getY() / 100);
            System.out.println("ѡ��");
        } else if (a.getCurole() == 1 && a.getCumove() != null) {
            if ((a.getCumove().getXx() == xx && Math.abs(a.getCumove().getYy() - yy) == 2 && a.pan[(a.getCumove().getYy() + yy) / 2][xx] == 0) || (a.getCumove().getYy() == yy && Math.abs(a.getCumove().getXx() - xx) == 2 && a.pan[yy][(a.getCumove().getXx() + xx) / 2] == 0)) {
                this.setVisible(false);
                a.getQi().remove(this);
                a.logMes("X:" + a.getCumove().getX() / 100 + ", Y:" + a.getCumove().getY() / 100 + "�Ե� " + "X:" + this.getX() / 100 + ", Y:" + this.getY() / 100);
                a.getCumove().setLocation(this.getLocation());
                a.pan[a.getCumove().getYy()][a.getCumove().getXx()] = 0;
                a.getCumove().setXx(this.getXx());
                a.getCumove().setYy(this.getYy());
                a.setCumove(null);
                this.getParent().remove(this);
                if (a.getQi().size() == 2) {
                    a.setResult(1);
                    a.logMes("�췽ʤ��");
                    Main.close("�췽ʤ��");
                }
                a.setCurole(1 - a.getCurole());
            }
        }
    }
}
