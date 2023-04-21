package GUI;

import core.Fruit;
import core.Item;
import core.Seed;
import core.Shop;
import core.Utility;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 *
 * @author user
 */
public class ShopOneDialog extends JDialog {

    private ArrayList<ItemLabel> arrLabel = new ArrayList<ItemLabel>();

    private int tileWidth = 100;

    private int tileHeight = 100;

    private int MAX_ITEM_WIDTH = 4;

    private int MAX_ITEM_HEIGHT = 4;

    private int minItemShowedIndex = 0;

    private ArrayList<Item> listItem = new ArrayList<Item>();

    Sound song = new Sound();

    public ShopOneDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public class ItemLabel extends JLabel {

        private int itemId = 1;

        public int getListItemId() {
            return itemId;
        }

        public void setListItemId(int val) {
            itemId = val;
        }
    }

    public void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);
        setSize(400, 500);
        if (Utility.getLevel() == 1) {
            listItem.add(new Seed(2));
            listItem.add(new Seed(5));
            listItem.add(new Seed(8));
            listItem.add(new Fruit(2));
            listItem.add(new Fruit(5));
            listItem.add(new Fruit(8));
        } else if (Utility.getLevel() == 2) {
            listItem.add(new Seed(2));
            listItem.add(new Seed(4));
            listItem.add(new Seed(5));
            listItem.add(new Seed(6));
            listItem.add(new Seed(8));
            listItem.add(new Fruit(2));
            listItem.add(new Fruit(4));
            listItem.add(new Fruit(5));
            listItem.add(new Fruit(6));
            listItem.add(new Fruit(8));
        } else if (Utility.getLevel() == 3) {
            listItem.add(new Seed(2));
            listItem.add(new Seed(3));
            listItem.add(new Seed(4));
            listItem.add(new Seed(5));
            listItem.add(new Seed(6));
            listItem.add(new Seed(7));
            listItem.add(new Seed(8));
            listItem.add(new Fruit(2));
            listItem.add(new Fruit(3));
            listItem.add(new Fruit(4));
            listItem.add(new Fruit(5));
            listItem.add(new Fruit(6));
            listItem.add(new Fruit(7));
            listItem.add(new Fruit(8));
        } else if (Utility.getLevel() == 4) {
            listItem.add(new Seed(2));
            listItem.add(new Seed(3));
            listItem.add(new Seed(4));
            listItem.add(new Seed(5));
            listItem.add(new Seed(6));
            listItem.add(new Seed(7));
            listItem.add(new Seed(8));
            listItem.add(new Seed(9));
            listItem.add(new Fruit(2));
            listItem.add(new Fruit(3));
            listItem.add(new Fruit(4));
            listItem.add(new Fruit(5));
            listItem.add(new Fruit(6));
            listItem.add(new Fruit(7));
            listItem.add(new Fruit(8));
            listItem.add(new Fruit(9));
        } else if (Utility.getLevel() == 5) {
            listItem.add(new Seed(1));
            listItem.add(new Seed(2));
            listItem.add(new Seed(3));
            listItem.add(new Seed(4));
            listItem.add(new Seed(5));
            listItem.add(new Seed(6));
            listItem.add(new Seed(7));
            listItem.add(new Seed(8));
            listItem.add(new Seed(9));
            listItem.add(new Fruit(1));
            listItem.add(new Fruit(2));
            listItem.add(new Fruit(3));
            listItem.add(new Fruit(4));
            listItem.add(new Fruit(5));
            listItem.add(new Fruit(6));
            listItem.add(new Fruit(7));
            listItem.add(new Fruit(8));
            listItem.add(new Fruit(9));
        }
        System.out.println(Utility.getLevel());
        int numLabel = 0;
        for (int i = 0; i < MAX_ITEM_HEIGHT; i++) {
            for (int j = 0; j < MAX_ITEM_WIDTH; j++) {
                if (numLabel >= listItem.size()) {
                    arrLabel.add(new ItemLabel());
                    arrLabel.get(numLabel).setBounds((j % MAX_ITEM_WIDTH) * tileWidth, (i % MAX_ITEM_HEIGHT) * tileHeight, tileWidth, tileHeight);
                    arrLabel.get(numLabel).setText("");
                    arrLabel.get(numLabel).setIcon(null);
                    arrLabel.get(numLabel).setListItemId(-1);
                } else {
                    arrLabel.add(new ItemLabel());
                    arrLabel.get(numLabel).setBounds((j % MAX_ITEM_WIDTH) * tileWidth, (i % MAX_ITEM_HEIGHT) * tileHeight, tileWidth, tileHeight);
                    arrLabel.get(numLabel).setText("<html>" + listItem.get(numLabel).getItemName() + "<br> harga beli : " + listItem.get(numLabel).getBuyingPrice() + "<br> harga jual :  " + listItem.get(numLabel).getSellingPrice() + "</html>");
                    arrLabel.get(numLabel).setIcon(new ImageIcon(getClass().getResource(listItem.get(numLabel).getImagePath())));
                    arrLabel.get(numLabel).setHorizontalTextPosition(JLabel.CENTER);
                    arrLabel.get(numLabel).setVerticalTextPosition(JLabel.BOTTOM);
                    arrLabel.get(numLabel).setListItemId(numLabel);
                    arrLabel.get(numLabel).addMouseListener(new java.awt.event.MouseAdapter() {

                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            System.out.println(((ItemLabel) evt.getSource()).getListItemId());
                            if (((ItemLabel) evt.getSource()).getListItemId() != -1) {
                                System.out.println(listItem.get(((ItemLabel) evt.getSource()).getListItemId()).getItemId());
                                String s = (String) JOptionPane.showInputDialog(Utility.getStartMenu().getGameForm(), "Insert item quantity : \n", "Shop A", JOptionPane.PLAIN_MESSAGE);
                                if ((s != null) && (s.length() > 0)) {
                                    try {
                                        int qty = Integer.parseInt(s);
                                        int id = listItem.get(((ItemLabel) evt.getSource()).getListItemId()).getItemId();
                                        Utility.getPlayer().buy((Shop) Utility.getMap(2), id, qty);
                                        song.sound9();
                                    } catch (Exception err) {
                                        JOptionPane.showMessageDialog((Component) evt.getSource(), err.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        }
                    });
                }
                add(arrLabel.get(numLabel));
                numLabel++;
            }
        }
        JButton butLeft = new JButton("<<");
        butLeft.setBounds(10, 400, 60, 30);
        butLeft.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (minItemShowedIndex >= MAX_ITEM_WIDTH * MAX_ITEM_HEIGHT) {
                    minItemShowedIndex -= MAX_ITEM_WIDTH * MAX_ITEM_HEIGHT;
                    refreshShopScreen();
                }
            }
        });
        add(butLeft);
        JButton butRight = new JButton(">>");
        butRight.setBounds(100, 400, 60, 30);
        butRight.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                minItemShowedIndex += MAX_ITEM_WIDTH * MAX_ITEM_HEIGHT;
                refreshShopScreen();
            }
        });
        add(butRight);
        JButton butSell = new JButton("Sell");
        butSell.setBounds(190, 400, 60, 30);
        butSell.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object[] possibilities = { "1", "2", "3" };
                String s = (String) JOptionPane.showInputDialog((Component) e.getSource(), "Slot Barang di tas yang ingin dijual : ", "Jual Barang", JOptionPane.PLAIN_MESSAGE, null, possibilities, "1");
                if (!s.isEmpty()) {
                    int slotnumber = Integer.parseInt(s);
                    slotnumber -= 1;
                    String sqty = (String) JOptionPane.showInputDialog(Utility.getStartMenu().getGameForm(), "Insert item quantity : \n", "Shop A", JOptionPane.PLAIN_MESSAGE);
                    if (!sqty.isEmpty()) {
                        try {
                            int qty = Integer.parseInt(sqty);
                            Utility.getPlayer().sell((Shop) Utility.getMap(2), slotnumber, qty);
                        } catch (Exception err) {
                            JOptionPane.showMessageDialog((Component) e.getSource(), err.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        add(butSell);
    }

    public void refreshShopScreen() {
        int numLabel = 0;
        for (int i = minItemShowedIndex; i < minItemShowedIndex + (MAX_ITEM_WIDTH * MAX_ITEM_HEIGHT); i++) {
            try {
                arrLabel.get(numLabel).setListItemId(i);
                arrLabel.get(numLabel).setText("<html>" + listItem.get(i).getItemName() + "<br> harga beli : " + listItem.get(i).getBuyingPrice() + "<br> harga jual :  " + listItem.get(numLabel).getSellingPrice() + "</html>");
                arrLabel.get(numLabel).setIcon(new ImageIcon(getClass().getResource(listItem.get(i).getImagePath())));
                arrLabel.get(numLabel).setHorizontalTextPosition(JLabel.CENTER);
                arrLabel.get(numLabel).setVerticalTextPosition(JLabel.BOTTOM);
            } catch (Exception e) {
                arrLabel.get(numLabel).setListItemId(-1);
                arrLabel.get(numLabel).setText("");
                arrLabel.get(numLabel).setIcon(null);
                arrLabel.get(numLabel).setHorizontalTextPosition(JLabel.CENTER);
                arrLabel.get(numLabel).setVerticalTextPosition(JLabel.BOTTOM);
            } finally {
                numLabel++;
            }
        }
    }

    public static void main(String[] args) {
        ShopOneDialog a = new ShopOneDialog(new JFrame(), true);
        a.setVisible(true);
    }
}
