package edu.jrous.shell.bash;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import edu.jrous.core.AbstractKernel;
import edu.jrous.core.AbstractShell;
import edu.jrous.core.DeviceActive;
import edu.jrous.shell.explorer.Constantes;
import edu.jrous.ui.Interface;
import edu.jrous.ui.MessagesWarnings;

/**
 * <p>Clase representa un shell de Linux</p>
 * @author Manuel Sako
 * @since 1.0  
 */
public class Bash extends AbstractShell {

    /**
	 * Constructor nulo
	 *
	 */
    public Bash() {
        super(null);
    }

    /**
	 * Constructor del Bash
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 *
	 */
    public Bash(Interface _owner) {
        super(_owner);
        super.nameShell = this.getClass().getName();
        this.begin();
        this.listener();
    }

    /**
	 * NUmero de serie de este Shell
	 */
    private static final long serialVersionUID = -2771817855870283923L;

    /**
	 * Configuracion del AbstractShell
	 *
	 */
    public void begin() {
        nameDevice.setBackground(Constantes.COLOR_BLACK);
        nameDevice.setForeground(Constantes.COLOR_WHITE);
        command.setBackground(Constantes.COLOR_BLACK);
        command.setForeground(Constantes.COLOR_WHITE);
        history_area.setBackground(Constantes.COLOR_BLACK);
        history_area.setForeground(Constantes.COLOR_WHITE);
    }

    /**
	 * Configuracion de los listener del Device
	 *
	 */
    public void listener() {
        command.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent evt) {
                try {
                    DeviceActive deActi = (DeviceActive) owner.getNetwork().getDeviceSelected();
                    kernel = (AbstractKernel) Class.forName(deActi.getNameKernel()).newInstance();
                    kernel.setInterface(owner, owner.panelShell.bash);
                    kernel.ejecutar(evt, command.getText());
                } catch (Exception e) {
                    owner.panelShell.hideShells();
                    new MessagesWarnings(owner, e.getClass().getName(), e.getMessage());
                }
            }
        });
    }

    public void hideShell() {
        setVisible(false);
    }

    /**
	 * Para volver al estado natural cuando se quiera traer los
	 *valores que estan grabados en los dispositivos
	 */
    public void reconstructor() {
        try {
            DeviceActive deActi = (DeviceActive) owner.getNetwork().getDeviceSelected();
            owner.panelShell.bash.command.setText("");
            kernel.setInterface(owner, this);
            kernel.creatorOSI(deActi);
            owner.panelShell.bash.history_area.setText(deActi.ram_memory);
            owner.panelShell.bash.nameDevice.setText(deActi.prompt);
            owner.toolMessage.message.setText("Dispositivo " + deActi.name);
        } catch (Exception e) {
        }
    }

    /**
	 * <p>Refresco de todo el shell</p>
	 */
    public void refresh() {
        super.refresh();
        nameDevice.setBackground(Constantes.COLOR_BLACK);
        nameDevice.setForeground(Constantes.COLOR_WHITE);
        command.setBackground(Constantes.COLOR_BLACK);
        command.setForeground(Constantes.COLOR_WHITE);
        history_area.setBackground(Constantes.COLOR_BLACK);
        history_area.setForeground(Constantes.COLOR_WHITE);
    }
}
