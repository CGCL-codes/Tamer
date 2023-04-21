package tasklist;

import java.net.URL;
import java.util.regex.*;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.util.Log;

public class TaskType {

    public TaskType() {
        setName("");
        setPattern("");
        setSample("");
        setIgnoreCase(false);
        setIconPath("Exclamation.gif");
    }

    public TaskType(String name, String pattern, String sample, boolean ignoreCase, String iconPath) {
        this(name, pattern, sample, ignoreCase, iconPath, true);
    }

    public TaskType(String name, String pattern, String sample, boolean ignoreCase, String iconPath, boolean active) {
        setName(name);
        setPattern(pattern);
        setSample(sample);
        setIgnoreCase(ignoreCase);
        setIconPath(iconPath);
        setActive(active);
        compileRE();
    }

    public Task extractTask(Buffer buffer, String tokenText, int line, int tokenOffset) {
        Matcher match = this.re.matcher(tokenText);
        if (!match.find()) {
            return null;
        }
        String identifier = match.group(1);
        String comment = match.group(2);
        String text = tokenText.substring(match.start(0));
        int start = tokenOffset + match.start(0) + 1;
        int end = start + text.length() - 1;
        return new Task(buffer, icon, line, identifier, comment, text, start, end);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        compileRE();
    }

    public String getSample() {
        return this.sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public boolean getIgnoreCase() {
        return this.ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        this.reFlags = ignoreCase ? Pattern.CASE_INSENSITIVE : 0;
        compileRE();
    }

    public Icon getIcon() {
        return this.icon;
    }

    public String getIconPath() {
        return this.iconPath;
    }

    public void setIconPath(String iconPath) {
        if (this.iconPath != iconPath || this.icon == null) {
            this.iconPath = iconPath;
            Icon _icon = TaskType.loadIcon(iconPath);
            if (_icon != null) this.icon = _icon;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean b) {
        active = b;
    }

    public int getREFlags() {
        return reFlags;
    }

    private void compileRE() {
        this.re = null;
        try {
            this.re = Pattern.compile(this.pattern, this.getREFlags());
        } catch (PatternSyntaxException e) {
            Log.log(Log.ERROR, TaskType.class, "Failed to compile task pattern: " + pattern + e.toString());
        }
    }

    public void save(int i) {
        jEdit.setProperty("tasklist.tasktype." + i + ".name", name);
        jEdit.setProperty("tasklist.tasktype." + i + ".pattern", pattern);
        jEdit.setProperty("tasklist.tasktype." + i + ".sample", sample);
        jEdit.setBooleanProperty("tasklist.tasktype." + i + ".ignorecase", ignoreCase);
        jEdit.setProperty("tasklist.tasktype." + i + ".iconpath", iconPath);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof TaskType) {
            TaskType type = (TaskType) obj;
            return getName().equals(type.getName());
        }
        return false;
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public String toString() {
        return this.pattern;
    }

    private Pattern re;

    private int reFlags;

    private String name;

    private String pattern;

    private String sample;

    private boolean ignoreCase;

    private String iconPath;

    private Icon icon;

    private boolean active;

    private boolean displayIdentifier = true;

    private static Hashtable<String, Icon> icons;

    /**
	* Loads an icon for later use
	* @param iconName a file name (start with 'file:' or resource path)
	*/
    public static Icon loadIcon(String iconName) {
        Icon icon = (Icon) icons.get(iconName);
        if (icon != null) return icon;
        if (iconName.startsWith("file:")) {
            icon = new ImageIcon(iconName.substring(5));
        } else {
            URL url = TaskListPlugin.class.getResource("/icons/" + iconName);
            if (url == null) {
                Log.log(Log.ERROR, TaskType.class, "TaskType.loadIcon() - icon not found: " + iconName);
                return null;
            }
            icon = new ImageIcon(url);
        }
        icons.put(iconName, icon);
        return icon;
    }

    static {
        icons = new Hashtable<String, Icon>();
        StringTokenizer st = new StringTokenizer(jEdit.getProperty("tasklist.icons"));
        while (st.hasMoreElements()) {
            String icon = st.nextToken();
            loadIcon(icon);
        }
    }
}
