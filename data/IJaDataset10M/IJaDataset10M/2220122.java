package org.openremote.controller.model;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import org.openremote.controller.command.CommandFactory;
import org.openremote.controller.command.ExecutableCommand;
import org.openremote.controller.Constants;
import org.openremote.controller.deployer.ModelBuilder;
import org.openremote.controller.protocol.ReadCommand;
import org.openremote.controller.utils.Logger;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * This is a command model that represents the command properties as an in-memory model for
 * rules, scripts, etc. This is different from the execution model that is currently defined
 * in the org.openremote.controller.command package (see ORCJAVA-209). <p>
 *
 * Until this command data model is merged with the execution model, this implementation is
 * somewhat 'anemic' that it either needs to delegate to the execution model in some cases or
 * the execution model may need to be used directly in some cases.  <p>
 *
 * This command model should always be immutable -- mutability of data model is driven by
 * the deployment lifecycle (and its backing XML document model) which should be re-read and
 * the object model rebuilt entirely when data modifications are required.
 *
 * @author <a href="mailto:juha@openremote.org">Juha Lindfors</a>
 */
public class Command {

    /**
   * Default value returned by {@link #getName()} if no name has been set in command's properties.
   */
    public static final String DEFAULT_NAME_PROPERTY_VALUE = "<no name>";

    /**
   * Command property name that contains a logical command name that can be used by scripts,
   * rules, REST API calls, etc.
   */
    public static final String COMMAND_NAME_PROPERTY = "name";

    /**
   * Common log category for runtime command execution.
   */
    private static final Logger log = Logger.getLogger(Constants.RUNTIME_COMMAND_EXECUTION_LOG_CATEGORY);

    /**
   * Command's protocol identifier, such as 'knx', 'x10', 'onewire', etc. This identifier is
   * used to determine which protocol builder plugin is used to construct executable commands.
   */
    private String protocolType;

    /**
   * A unique command identifier. This corresponds to the 'id' attribute in controller XML
   * definition's {@code <command>} element.
   */
    private int id;

    /**
   * List of generic command properties. The correspond to {@code <property>} elements nested
   * within {@code <command>} element in controller's XML definition. <p>
   *
   * An arbitrary list of properties is allowed. Specific property names (such as
   * {@link #COMMAND_NAME_PROPERTY}) may have a special meaning. <p>
   *
   * NOTE: property *names* are *not* case sensitive. All property names are converted to lower
   * case.
   */
    private Map<String, String> properties;

    /**
   * Command factory delegates creation of command execution model to specific protocol specific
   * builders (Such as X10, KNX, one-wire, etc.)
   */
    private CommandFactory commandFactory;

    /**
   * Constructs a command data model.
   *
   * @param commandFactory
   *            Command factory is used to construct a protocol specific command execution model
   *            (for example, KNX specific commands or X10 specific commands).
   *
   * @param id
   *            This represents the unique identifier of the command element in controller's
   *            XML definition (corresponding to 'id' attribute of {@code <command>} element)
   *
   * @param type
   *            Protocol type identifier for this command. The protocol type identifier is used
   *            with the command factory to identify which plugin (Java bean implementation) is
   *            used to construct command's protocol specific execution model.
   *
   * @param properties
   *            Command's properties. Arbitrary list of properties that can be used to configure
   *            command instances. Certain property values such as {@link #COMMAND_NAME_PROPERTY}
   *            may have special meaning. The property name,value pairs match the
   *            {@code <property>} elements nested within {@code <command>} elements within
   *            controller's XML document model.
   */
    public Command(CommandFactory commandFactory, int id, String type, Map<String, String> properties) {
        this.id = id;
        this.protocolType = type;
        this.properties = properties;
        this.commandFactory = commandFactory;
    }

    /**
   * Returns a command's name property if present.
   *
   * @return    commands name, or a default {@link #DEFAULT_NAME_PROPERTY_VALUE} string if
   *            name property is not present
   */
    public String getName() {
        String name = getProperty(COMMAND_NAME_PROPERTY);
        if (name == null || name.equals("")) {
            return DEFAULT_NAME_PROPERTY_VALUE;
        }
        return name;
    }

    /**
   * Returns a command property value.
   *
   * @param name    Name of the command property to return. Note that command property names are
   *                not case-sensitive. All names are converted to lower case characters.
   *
   * @return        Command property value, or empty string if not found.
   */
    public String getProperty(String name) {
        String value = properties.get(name.toLowerCase());
        return (value == null) ? "" : value;
    }

    /**
   * Executes a command. This normally means a device protocol is used to communicate with
   * some physical device.
   */
    public void execute() {
        execute(null);
    }

    /**
   * Executes a parameterized command. This normally means a device protocol is used to
   * communicate with some physical device. Command parameterization is typically used
   * with commands that allow setting distinct values, such as setting a volume to a specific
   * distinct level, setting blinds to a specific distinct location, etc.
   *
   * @param param       command parameter value
   */
    public void execute(String param) {
        try {
            Element commandElement = constructXMLModel();
            if (param != null) {
                commandElement.setAttribute(org.openremote.controller.command.Command.DYNAMIC_VALUE_ATTR_NAME, param);
            }
            org.openremote.controller.command.Command cmd = commandFactory.getCommand(commandElement);
            if (cmd instanceof ExecutableCommand) {
                ExecutableCommand writeCommand = (ExecutableCommand) cmd;
                writeCommand.send();
            } else if (cmd instanceof ReadCommand) {
                log.error("Execution of read commands not implemented yet.");
            } else {
                log.error("Cannot execute {0}", this);
            }
        } catch (Throwable t) {
            log.error("Unable to execute command : {0} \n {1}", t, t.getMessage(), this);
        }
    }

    /**
   * Prints command id, protocol type and property map.
   *
   * @return  command data as string
   */
    @Override
    public String toString() {
        return "Command( ID = " + id + ", Type = " + protocolType + ", Properties : " + properties + ")";
    }

    /**
   * Constructs the corresponding XML element representing this command's data model. Mostly
   * this is used to satisfy the poorly designed command execution API.
   *
   * @return    JDOM Element representing this command's data model.
   */
    private Element constructXMLModel() {
        Namespace ns = ModelBuilder.SchemaVersion.OPENREMOTE_NAMESPACE;
        Element command = new Element("command", ns);
        command.setAttribute("id", Integer.toString(id));
        command.setAttribute("protocol", protocolType);
        Set<Element> childElements = new HashSet<Element>();
        for (String propertyKey : properties.keySet()) {
            Element property = new Element("property");
            property.setAttribute(COMMAND_NAME_PROPERTY, propertyKey);
            property.setAttribute("value", properties.get(propertyKey));
            childElements.add(property);
        }
        command.addContent(childElements);
        return command;
    }
}
