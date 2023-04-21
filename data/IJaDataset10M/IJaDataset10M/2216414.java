package org.pachyderm.migrationtool;

public interface ContinueListener {

    /**
   * Called by controller when the continue button is clicked. Intended so screen class can make
   * sure it is ok to continue before continuing.
   *
   * @return boolean true if ok to continue, false if it is not.
   */
    public boolean continueButtonClicked();
}
