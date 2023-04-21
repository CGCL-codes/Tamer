package com.nullfish.lib.resource;

/**
 * 文字列リソースを抽象化するインターフェイス
 * 
 * @author shunji
 */
public interface ResourceManager {

    /**
	 * 文字列リソースを取得する。
	 * @param name
	 * @return
	 */
    public String getResource(String name);
}
