package org.freeland.tool;

public abstract class Constants {

    public static final int TILEWIDTH = 32;

    public static final int TILEHEIGHT = 32;

    public abstract class Tmx {

        public static final String BACKGROUND_INDEX_NAME = "backgroundIndex";

        public static final String XPATH_PROPERTIES = "/map/properties/property";

        public static final String XPATH_LAYERS = "/map/layer";

        public static final String XPATH_TILESETS = "/map/tileset";

        public static final String NAME = "name";

        public static final String VALUE = "value";

        public static final String WIDTH = "width";

        public static final String HEIGHT = "height";

        public static final String GID = "gid";

        public static final String FIRSTGID = "firstgid";

        public static final String XPATH_LAYER_PROPERTIES = "properties/property";

        public static final String XPATH_LAYER_TILES = "data/tile";

        public static final String LAYER_SCROLL_SPEED_NAME = "scrollSpeed";

        public static final String LAYER_TILE_INDEX_NAME = "tileIndex";

        public static final String LAYER_TYPE_NAME = "type";

        public static final String LAYER_TILESET = "tileSet";

        public static final String DTDURL = "http://mapeditor.org/dtd/1.0/map.dtd";
    }

    public abstract class TmxNodeNames {

        public static final String ROOT = "map";

        public static final String PROPERTIES = "properties";

        public static final String PROPERTY = "property";

        public static final String TILESET = "tileset";

        public static final String IMAGE = "image";

        public static final String LAYER = "layer";

        public static final String DATA = "data";

        public static final String TILE = "tile";

        public abstract class RootNode {

            public static final String VERSION = "version";

            public static final String ORIENTATION = "orientation";

            public static final String WIDTH = "width";

            public static final String HEIGTH = "height";

            public static final String TILEWIDTH = "tilewidth";

            public static final String TILEHEIGHT = "tileheight";
        }

        public abstract class TilesetNode {

            public static final String FIRSTGID = "firstgid";

            public static final String TILEWIDTH = "tilewidth";

            public static final String TILEHEIGHT = "tileheight";

            public static final String NAME = "name";

            public abstract class ImageNode {

                public static final String SOURCE = "source";

                public static final String TRANS = "trans";
            }
        }

        public abstract class LayerNode {

            public static final String NAME = "name";

            public static final String WIDTH = "width";

            public static final String HEIGHT = "height";

            public abstract class DataNode {

                public abstract class TileNode {

                    public static final String GID = "gid";
                }
            }
        }
    }
}
