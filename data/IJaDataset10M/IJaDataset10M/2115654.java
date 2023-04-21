package voorraadbeheer.swing.panels;

import gilgamesh.beans.Interact;
import gilgamesh.swing.Name;
import gilgamesh.swing.RiverComponent;
import gilgamesh.swing.RiverLayoutManager;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import voorraadbeheer.guts.Mode;
import voorraadbeheer.model.Produkt;
import assistedinject.Assisted;
import assistedinject.AssistedInject;
import com.google.inject.Inject;

@SuppressWarnings("unused")
@RiverLayoutManager
public class ProduktPanel extends InteractivePanel<Produkt> {

    private static final long serialVersionUID = 1L;

    @Name("produkt.artikelNummer.label")
    @RiverComponent("p left")
    private final JLabel artikelNummerLabel;

    @Interact(attach = "text", with = "artikelNummer")
    @RiverComponent("tab hfill")
    private final JTextField artikelNummer;

    @Name("produkt.omschrijving.label")
    @RiverComponent("p left")
    private final JLabel omschrijvingLabel;

    @Interact(attach = "text", with = "omschrijving")
    @RiverComponent("tab hfill vfill")
    private final JTextField omschrijving;

    @RiverComponent("p center")
    private final JPanel buttons;

    @AssistedInject
    @Inject
    public ProduktPanel(@Assisted final Mode mode, final PanelUtils panelUtils, final JLabel artikelNummerLabel, final JTextField artikelNummer, final JLabel omschrijvingLabel, final JTextField omschrijving) {
        super(Produkt.class);
        this.artikelNummerLabel = artikelNummerLabel;
        this.artikelNummer = artikelNummer;
        this.omschrijvingLabel = omschrijvingLabel;
        this.omschrijving = omschrijving;
        this.buttons = panelUtils.buttons(mode);
    }
}
