package com.memoire.fu;

public interface FuCompletor {

    /**
   * Returns the completing sequence.
   * @param _s the whole buffer
   * @param _p the current cursor position
   * @return the characters to insert
   */
    String complete(String _s, int _p);
}
