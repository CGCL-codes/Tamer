package com.blah.gwtgames.client.sudoku.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SudokuServiceAsync {

    void getPuzzle(int id, AsyncCallback callback);
}
