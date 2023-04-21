package client.gui.dialog;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.BoxLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import client.MWClient;
import client.gui.InnerStellarMap;
import common.util.SpringLayoutHelper;
import common.Planet;

public class PlanetSearchDialog extends JDialog implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = -7897295866660184584L;

    private final InnerStellarMap map;

    private final Collection<Planet> planets;

    private final TreeSet<String> planetNames;

    private JList matchingPlanetsList;

    private JScrollPane scrollPane;

    private JTextField nameField;

    private final JButton okayButton = new JButton("OK");

    private final JButton cancelButton = new JButton("Cancel");

    private final String okayCommand = "Okay";

    public PlanetSearchDialog(InnerStellarMap map, MWClient mwclient) {
        super(mwclient.getMainFrame(), "Planet Search", true);
        this.map = map;
        this.planets = mwclient.getData().getAllPlanets();
        planetNames = new TreeSet<String>();
        for (Iterator<Planet> it = planets.iterator(); it.hasNext(); ) planetNames.add(it.next().getName());
        final Object[] allPlanetNames = planetNames.toArray();
        matchingPlanetsList = new JList(allPlanetNames);
        matchingPlanetsList.setVisibleRowCount(20);
        matchingPlanetsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nameField = new JTextField();
        nameField.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent e) {
                new Thread() {

                    @Override
                    public void run() {
                        String text = nameField.getText();
                        if (text == null || text.equals("")) {
                            matchingPlanetsList.setListData(allPlanetNames);
                            return;
                        }
                        ArrayList<String> possiblePlanets = new ArrayList<String>();
                        text = text.toLowerCase();
                        for (Iterator<String> it = planetNames.iterator(); it.hasNext(); ) {
                            String curPlanet = it.next();
                            if (curPlanet.toLowerCase().indexOf(text) != -1) possiblePlanets.add(curPlanet);
                        }
                        matchingPlanetsList.setListData(possiblePlanets.toArray());
                        boolean shouldContinue = true;
                        int element = 0;
                        Iterator<String> it = possiblePlanets.iterator();
                        while (it.hasNext() && shouldContinue) {
                            String name = it.next();
                            if (name.toLowerCase().startsWith(text)) {
                                matchingPlanetsList.setSelectedIndex(element);
                                shouldContinue = false;
                            }
                            element++;
                        }
                        if (shouldContinue) {
                            matchingPlanetsList.setSelectedIndex(0);
                        }
                    }
                }.start();
            }
        });
        scrollPane = new JScrollPane(matchingPlanetsList);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        okayButton.setActionCommand(okayCommand);
        okayButton.addActionListener(this);
        cancelButton.addActionListener(this);
        JPanel springPanel = new JPanel(new SpringLayout());
        springPanel.add(nameField);
        springPanel.add(scrollPane);
        SpringLayoutHelper.setupSpringGrid(springPanel, 2, 1);
        JPanel buttonFlow = new JPanel();
        buttonFlow.add(okayButton);
        buttonFlow.add(cancelButton);
        JPanel generalLayout = new JPanel();
        generalLayout.setLayout(new BoxLayout(generalLayout, BoxLayout.Y_AXIS));
        generalLayout.add(springPanel);
        generalLayout.add(buttonFlow);
        this.getContentPane().add(generalLayout);
        this.pack();
        this.checkMinimumSize();
        this.setResizable(true);
        this.getRootPane().setDefaultButton(okayButton);
        this.setLocationRelativeTo(null);
    }

    /**
	 * OK or CANCEL buttons pressed. Handle any
	 * changes and then close the dialouge.
	 */
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals(okayCommand)) {
            String selectedPlanet = (String) matchingPlanetsList.getSelectedValue();
            if (selectedPlanet == null) selectedPlanet = nameField.getText();
            if (selectedPlanet == null || selectedPlanet.equals("")) return;
            if (matchingPlanetsList.getModel().getSize() == 1) selectedPlanet = (String) matchingPlanetsList.getModel().getElementAt(0);
            for (Iterator<Planet> it = planets.iterator(); it.hasNext(); ) {
                Planet planet = it.next();
                if (selectedPlanet.equals(planet.getName())) {
                    map.setSelectedPlanet(planet);
                    map.activate(planet, true);
                    map.saveMapSelection(planet);
                    this.dispose();
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Unknown Planet");
        }
        this.dispose();
    }

    private void checkMinimumSize() {
        Dimension curDim = this.getSize();
        int height = 0;
        int width = 0;
        boolean shouldRedraw = false;
        if (curDim.getWidth() < 300) {
            width = 300;
            shouldRedraw = true;
        } else width = (int) curDim.getWidth();
        if (curDim.getHeight() < 300) {
            height = 300;
            shouldRedraw = true;
        } else height = (int) curDim.getHeight();
        if (shouldRedraw) {
            this.setSize(new Dimension(width, height));
        }
    }
}
