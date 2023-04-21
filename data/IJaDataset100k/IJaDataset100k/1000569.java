package org.cyberaide.calendar;

/**
 * a java bean representing a job
 * 
 */
public class Job {

    private String id;

    private String name;

    private String label;

    private String keywords;

    private String group;

    private String type;

    private String dependency;

    private String executable_command;

    private String executable_arguments;

    private String executable_environment;

    private String executable_stdin;

    private String executable_stdout;

    private String executable_input_files;

    private String executable_output_files;

    private String executable_status;

    private String executable_directory;

    private String resource_hostname;

    private String resource_starttime;

    private String resource_endtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getExecutable_arguments() {
        return executable_arguments;
    }

    public void setExecutable_arguments(String executable_arguments) {
        this.executable_arguments = executable_arguments;
    }

    public String getExecutable_command() {
        return executable_command;
    }

    public void setExecutable_command(String executable_command) {
        this.executable_command = executable_command;
    }

    public String getExecutable_directory() {
        return executable_directory;
    }

    public void setExecutable_directory(String executable_directory) {
        this.executable_directory = executable_directory;
    }

    public String getExecutable_environment() {
        return executable_environment;
    }

    public void setExecutable_environment(String executable_environment) {
        this.executable_environment = executable_environment;
    }

    public String getExecutable_input_files() {
        return executable_input_files;
    }

    public void setExecutable_input_files(String executable_input_files) {
        this.executable_input_files = executable_input_files;
    }

    public String getExecutable_output_files() {
        return executable_output_files;
    }

    public void setExecutable_output_files(String executable_output_files) {
        this.executable_output_files = executable_output_files;
    }

    public String getExecutable_status() {
        return executable_status;
    }

    public void setExecutable_status(String executable_status) {
        this.executable_status = executable_status;
    }

    public String getExecutable_stdin() {
        return executable_stdin;
    }

    public void setExecutable_stdin(String executable_stdin) {
        this.executable_stdin = executable_stdin;
    }

    public String getExecutable_stdout() {
        return executable_stdout;
    }

    public void setExecutable_stdout(String executable_stdout) {
        this.executable_stdout = executable_stdout;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getResource_endtime() {
        return resource_endtime;
    }

    public void setResource_endtime(String resource_endtime) {
        this.resource_endtime = resource_endtime;
    }

    public String getResource_hostname() {
        return resource_hostname;
    }

    public void setResource_hostname(String resource_hostname) {
        this.resource_hostname = resource_hostname;
    }

    public String getResource_starttime() {
        return resource_starttime;
    }

    public void setResource_starttime(String resource_starttime) {
        this.resource_starttime = resource_starttime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String print(String attrName) {
        String result;
        try {
            result = (String) (this.getClass().getMethod("get" + attrName, null).invoke(this, null));
        } catch (Exception e) {
            return "";
        }
        if ((result == null) || (result.trim().length() == 0)) return ""; else return attrName + ": " + result + "\n";
    }

    public static void main(String argv[]) {
        Job job = new Job();
        job.setName("123");
        job.setLabel("rrr");
        System.out.println(job.print("Name"));
        System.out.println(job.print("Type"));
        System.out.println(job.print("Label"));
    }
}
