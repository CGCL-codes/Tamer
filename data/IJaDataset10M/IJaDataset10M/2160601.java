package bsh;

/**
	A File that supports localized paths using the bsh current working 
	directory (bsh.cwd)

	Also adds basename/dirname functionality.

	Note: This kind of crosses the line between the core interpreter and
	the "util" package.  It's here so that we're consistent in the source()
	feature of the interpreter.
*/
class File extends java.io.File {

    String dirName = "", baseName;

    File(String fileName) {
        super(fileName);
        int i = fileName.lastIndexOf(File.separator);
        if (i != -1) {
            dirName = fileName.substring(0, i);
            baseName = fileName.substring(i + 1);
        } else baseName = fileName;
    }

    public String dirName() {
        return dirName;
    }

    public String baseName() {
        return baseName;
    }

    public String toString() {
        return super.toString() + ", dirName = " + dirName + ", baseName = " + baseName;
    }
}
