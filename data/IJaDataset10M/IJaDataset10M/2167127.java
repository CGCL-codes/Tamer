package com.app2go.sudokufree.source;

import android.content.Context;
import android.content.res.AssetManager;
import com.app2go.sudokufree.db.SudokuDatabase;

public class PuzzleSourceResolver {

    private PuzzleSourceResolver() {
    }

    public static PuzzleSource resolveSource(Context context, String puzzleSourceId) throws PuzzleIOException {
        if (PuzzleSourceIds.isAssetSource(puzzleSourceId)) return resolveAssetSource(context, PuzzleSourceIds.getAssetFolderName(puzzleSourceId));
        if (PuzzleSourceIds.isDbSource(puzzleSourceId)) return resolveDbSource(context, PuzzleSourceIds.getDbFolderId(puzzleSourceId));
        throw new IllegalArgumentException(puzzleSourceId);
    }

    private static PuzzleSource resolveAssetSource(Context context, String folderName) throws PuzzleIOException {
        AssetManager assets = context.getAssets();
        return new AssetsPuzzleSource(assets, folderName);
    }

    private static PuzzleSource resolveDbSource(Context context, long folderId) throws PuzzleIOException {
        SudokuDatabase db = new SudokuDatabase(context);
        return new DbPuzzleSource(db, folderId);
    }
}
