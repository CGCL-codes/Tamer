package layer.disseminator.disseminationManagement;

import java.util.Vector;
import layer.ALayer;
import layer.AProtocol;
import layer.IDisplayUpdater;
import layer.IStartable;
import org.apache.log4j.Logger;
import combasics.event.LayerEvent;
import combasics.event.ModelEvent;
import combasics.eventDispatcher.Model_EventDispatcher;
import combasics.listener.ModelListener;
import config.PropertyFileLoader.IPropertyChangeEventSource;

/**
 * Die Verteilungskomponente (VK) ist f�r die Verwaltung und Verteilung von
 * Datenobjekten zust�ndig. F�r die Verteilung stehen die folgenden
 * Verteilungsprotokolle zur Verf�gung:
 * <ol>
 * <li><code>SimpleFlooding</code>
 * <li><code>ProbabilisticFlooding</code>
 * <li><code>AdaptiveProbabilisticFlooding</code>
 * </ol>
 * <p>
 * Jedes dieser Protokolle l�uft in einem eigenst�ndigen Prozess im Hintergrund.
 * Alle f�r die VK bestimmten Nachrichten werden von ihr entgegengenommen und
 * den Protokollen zugestellt. Das jeweilige Protokoll sorgt f�r die
 * entsprechende Bearbeitung.
 * <p>
 * Die VK erzeugt f�r jedes Objekt einen eigenen <code>SF_PDU</code>, der den
 * folgenden Parameter enth�lt:
 * <ul>
 * <li><code>iDissProtocol</code>- Gibt das Protokoll an, das diesen Rahmen
 * bearbeiten soll
 * </ul>
 * Dar�ber hinaus enth�lt er die serialisierten Nutzdaten. Weitere
 * Informationen: {@link  serializableBeans.layerBeans.impl.DisseminationFrame}
 * <p>
 * An dem Objekt h�ngen die folgenden objektspezifischen Parameter:
 * <ul>
 * <li><code>iId</code>- Objektidentifikationsnummer
 * <li><code>iHops</code>- Anzahl der Hops �ber die verteilt wird
 * <li><code>dProbability</code>- Wahrscheinlichkeit mit der verteilt wird
 * <li><code>iNumberOfNeigbours</code>- Anzahl der Nachbarn des Senders
 * </ul>
 * Weitere Informationen:
 * {@link  layer.disseminator.disseminationManagement.DisseminationCapsule}
 * <p>
 * Damit ergibt sich das folgende allgemeine Rahmenformat: <blockquote><table
 * border="0" bgcolor="#D4D7CD">
 * <tr>
 * <th colspan="3" align=left><code>SF_PDU</code></th>
 * </tr>
 * <tr>
 * <td><code>iDissProtocol</code>|</td>
 * <td><table border="0" bgcolor="#B5A9BB">
 * <tr>
 * <td><code>abyPayload</code></td>
 * </tr>
 * </table></td>
 * </tr>
 * </table> </blockquote>
 * <p>
 * Die VK sitzt auf dem Kommunikationsstack und empf�ngt �ber die Schnittstelle
 * <code>LayerListener</code> spezielle Ereignisse, die von der obersten
 * Schicht, dem <code>Dispatcher</code>, ihr zugeteilt werden.
 * </p>
 * <p>
 * Die VK dient als Modell und empf�ngt �ber die Schnittstelle
 * <code>ControllerListener</code> spezielle Ereignisse, die von einem UI
 * erzeugt werden und wor�ber sie gesteuert werden kann.
 * </p>
 * <p>
 * Die VK erweitert die Klasse <code>TwoChaLayer_Model_EventDispatcher</code>,
 * wodurch sie einerseits Schichten andererseits GUIs erm�glicht, sich f�r das
 * Empfangen von den jeweiligen Ereignissen bei ihr zu registrieren.
 * </p>
 * Folgende Skizze verdeutlicht diesen Baustein: <blockquote>
 * 
 * <pre>
 *                                                                                                  -----------------------------
 *                                                                                                  |  LLIS1, LLIS2, ..., LLISn |
 *                                                                                                  |    &circ;      &circ;           &circ;   |
 *                                                                                                  -----|------|-----------|----
 *                                                                                                |------|------|----...----|---- |
 *                                                                                                |  | L_Ev0  L_Ev0       L_Ev0 | |  |-------|
 *                                                                                               --  ------------Out2------------ |  |       |
 *                                                                                         LayerListener                          ----&gt;MLIS1,|
 *                                                                                                |  ..., L_Ev3, L_Ev2, L_Ev1,    |  |       |
 *                                                                                                |                               ----&gt;MLIS2,|
 *                                                                                                |          DMLayer         |  |   .   |
 *                                                                                                |                               |  |   .   |
 *                                                                                               --  ..., L_Ev3, L_Ev2, L_Ev1,    |  |   .   |
 *                                                                                        ControllerListener                      ----&gt;MLISn |
 *                                                                                               --  ------------Out1------------ |  |       |
 *                                                                                                |  | L_Ev0  L_Ev0       L_Ev0 | |  |-------|
 *                                                                                                |------|------|----...----|-----|
 *                                                                                                  -----|------|----...----|----
 *                                                                                                  |    v      v           v   |
 *                                                                                                  |  LLIS1, LLIS2, ..., LLISn |
 *                                                                                                  -----------------------------
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Yark Schroeder, Manuel Scholz
 * @version $Id: DMLayer.java,v 1.9 2006/04/04 13:39:56 yark Exp $
 * @since 1.3
 */
public class DMLayer extends ALayer implements IDisplayUpdater, IStartable, IDMLayerDissModel, IDMLayerNHModel {

    private static Logger mLogger = Logger.getLogger(DMLayer.class.getName());

    /**
	 * Adapter, an den sich Objekte anmelden, um <code>ModellEvent</code>s zu
	 * empfangen.
	 */
    private Model_EventDispatcher mDisModel_EventDispatcher;

    /**
	 * Adapter, an den sich Objekte anmelden, um <code>ModellEvent</code>s zu
	 * empfangen.
	 */
    private Model_EventDispatcher mNhModel_EventDispatcher;

    /**
	 * Implementierung des Push-Protokolls "Einfaches Fluten".
	 */
    private SFProtocol mSFProtocol;

    /**
	 * Implementierung des Push-Protokolls "Probabilistisches Fluten".
	 */
    private PFProtocol mPFProtocol;

    /**
	 * Implementierung des Push-Protokolls "Adaptives Probabilistisches Fluten".
	 */
    private APFProtocol mAPFProtocol;

    /**
	 * Sp�rt Nachbarn auf und verwaltet diese.
	 */
    private NHProtocol mNHProtocol;

    private AProtocol[] mProtocolInstances;

    private Vector mDMHistory;

    /**
	 * Erstellt eine Verteilungskomponente mit Default-Konfiguration.
	 * 
	 * @since 1.3
	 */
    public DMLayer() {
        init();
    }

    public void processLayerEventForDownwardTransport(LayerEvent kEvent) {
        if (mLogger.isDebugEnabled()) {
            mLogger.debug("got: " + kEvent.toString());
        }
        for (int i = 0; i < mProtocolInstances.length; i++) {
            if (mProtocolInstances[i].isReceiver(kEvent.getDataUnit())) {
                mProtocolInstances[i].handleOutgoingDataUnit(kEvent.getDataUnit());
                break;
            }
        }
    }

    public void processLayerEventForUpwardTransport(LayerEvent kEvent) {
        if (mLogger.isDebugEnabled()) {
            mLogger.debug("got: " + kEvent.toString());
        }
        for (int i = 0; i < mProtocolInstances.length; i++) {
            if (mProtocolInstances[i].isReceiver(kEvent.getDataUnit())) {
                mProtocolInstances[i].handleIncomingDataUnit(kEvent.getDataUnit());
                break;
            }
        }
    }

    public void start() {
        mSFProtocol.start();
        mPFProtocol.start();
        mAPFProtocol.start();
        mNHProtocol.start();
    }

    public void stop() {
        mSFProtocol.stop();
        mPFProtocol.stop();
        mAPFProtocol.stop();
        mNHProtocol.stop();
    }

    public void updateView(ModelEvent kEvent) {
        switch(kEvent.getID()) {
            case ModelEvent.FP_HISTORY_CHANGED:
                mDisModel_EventDispatcher.dispatchModelEvent(kEvent);
                break;
            case ModelEvent.NH_HISTORY_CHANGED:
                mNhModel_EventDispatcher.dispatchModelEvent(kEvent);
                break;
            case ModelEvent.NH_PROGRESS_ABORTED:
                mNhModel_EventDispatcher.dispatchModelEvent(kEvent);
                break;
            case ModelEvent.NH_PROGRESS_START:
                mNhModel_EventDispatcher.dispatchModelEvent(kEvent);
                break;
            default:
                break;
        }
    }

    public void clearHistory() {
        mAPFProtocol.clearHistory();
        mSFProtocol.clearHistory();
        mPFProtocol.clearHistory();
    }

    public Object[] getHistory() {
        return mDMHistory.toArray();
    }

    public void addDML_DisModelListener(ModelListener kListener) {
        mDisModel_EventDispatcher.addModelListener(kListener);
    }

    public void removeDML_DisModelListener(ModelListener kListener) {
        mDisModel_EventDispatcher.removeModelListener(kListener);
    }

    public void registerPropertyFileChangeListener(IPropertyChangeEventSource kPropertyManager) {
        mAPFProtocol.registerForPropertyChangeEvents(kPropertyManager);
        mNHProtocol.registerForPropertyChangeEvents(kPropertyManager);
        mPFProtocol.registerForPropertyChangeEvents(kPropertyManager);
        mSFProtocol.registerForPropertyChangeEvents(kPropertyManager);
    }

    /**
	 * Initialisierung dieser Klasse.
	 * 
	 * @since 1.3
	 */
    private void init() {
        mDMHistory = new Vector();
        mNHProtocol = new NHProtocol(this, this);
        mSFProtocol = new SFProtocol(this, this, mDMHistory);
        mPFProtocol = new PFProtocol(this, this, mDMHistory);
        mAPFProtocol = new APFProtocol(this, this, mNHProtocol, mDMHistory);
        mProtocolInstances = new AProtocol[4];
        mProtocolInstances[0] = mSFProtocol;
        mProtocolInstances[1] = mPFProtocol;
        mProtocolInstances[2] = mAPFProtocol;
        mProtocolInstances[3] = mNHProtocol;
        mDisModel_EventDispatcher = new Model_EventDispatcher();
        mNhModel_EventDispatcher = new Model_EventDispatcher();
    }

    public Object[] getNeighbourList() {
        return mNHProtocol.getNeighbourList();
    }

    public void neighbourCall() {
        mNHProtocol.call();
    }

    public void startIntervallNeighbourCalling() {
        mNHProtocol.startIntervallCalling();
    }

    public void stopIntervallNeighbourCalling() {
        mNHProtocol.stopIntervallCalling();
    }

    public void addDML_NhModelListener(ModelListener kListener) {
        mNhModel_EventDispatcher.addModelListener(kListener);
    }

    public void removeDML_NhModelListener(ModelListener kListener) {
        mNhModel_EventDispatcher.removeModelListener(kListener);
    }
}
