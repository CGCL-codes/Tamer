package Forms;

import Kino;
import Sale.*;
import Transactions.*;
import Error.Error;
import Forms.StdComponents.DefaultView;
import Forms.StdComponents.ErrorDialog;
import Forms.StdComponents.EnterCodeDialog;

/** repräsentiert DefaultForm des Kinos */
public class DefaultForm extends FormSheet
{
  public final static int TT_ERR_KeinePlatzgruppenVorhanden = 1;
  public final static int TT_ERR_KeineErm„áigungenVorhanden = 2;
  public final static int TT_ERR_KeineVorstellungVorhanden = 3;
  private int errorCode;

  /** erzeugt neues DefaultForm */
  public DefaultForm()
  {
    super("Startfenster",new DefaultView());

    removeAllButtons();

    addButton(30,"Karte(n) kaufen",
      new Action()
      {
        public void doAction(SalesPoint sp)
        {
          errorCode = getErrorCode();
          if (errorCode == 0)
          {
            // kein Fehler aufgetreten
            sp.runTransaction(new BuyTicket("Karte(n) kaufen"));
          }
          else
            {
              // Fehler
              new ErrorDialog(getOwner().getDisplayFrame(),getErrorMsg(errorCode));
            }
        }
      });

    addButton(31,"Karte(n) reservieren",
      new Action()
      {
        public void doAction(SalesPoint sp)
        {
          errorCode = getErrorCode();
          if (errorCode == 0)
          {
            // kein Fehler aufgetreten
            sp.runTransaction(new ReserveTicket("Karte(n) reservieren"));
          }
          else
            {
              // Fehler
              new ErrorDialog(getOwner().getDisplayFrame(),getErrorMsg(errorCode));
            }
        }
      });

    addButton(32,"reservierte Karte(n) abholen",
      new Action()
      {
        public void doAction(SalesPoint sp)
        {
          new EnterCodeDialog(getOwner().getDisplayFrame());
        }
      });

    addButton(33,"Karte(n) rückkaufen",
      new Action()
      {
        public void doAction(SalesPoint sp)
        {
          errorCode = getErrorCode();
          if (errorCode == 0)
          {
            // kein Fehler aufgetreten
            sp.runTransaction(new ReturnTicket("Karte(n) rückkaufen"));
          }
          else
            {
              // Fehler
              new ErrorDialog(getOwner().getDisplayFrame(),getErrorMsg(errorCode));
            }
        }
      });
  }

  /** liefert Fehlernummer */
  private int getErrorCode()
  {
    int error = 0;

    if (Kino.getSeatCategorys().getCatalog().size() == 0) error = TT_ERR_KeinePlatzgruppenVorhanden;
    if (Kino.getReductions().getCatalog().size() == 0) error = TT_ERR_KeineErm„áigungenVorhanden;
    if (Kino.getCinemaGuide().getCatalog().size() == 0) error = TT_ERR_KeineVorstellungVorhanden;

    return error; 
  }

  /** liefert Fehlermeldung zum Fehler errorCode */
  public String getErrorMsg(int errorCode)
  {
    switch(errorCode)
    {
      case TT_ERR_KeinePlatzgruppenVorhanden: return " Keine Platzgruppen vorhanden.\n" + 
                                                     " Sie müssen erst Platzgruppen eingeben";
      case TT_ERR_KeineErm„áigungenVorhanden: return " Keine Ermäßigungen vorhanden.\n" +
                                                     " Sie müssen erst Ermäßigungen eingeben";
      case TT_ERR_KeineVorstellungVorhanden: return " Es sind leider keine Vorstellungen vorhanden.";

      default: return " unbekannter Fehler";
    }
  }
}