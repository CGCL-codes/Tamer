package com.apocalyptech.minecraft.xray;

import com.apocalyptech.minecraft.xray.dtf.CompoundTag;
import com.apocalyptech.minecraft.xray.dtf.StringTag;
import com.apocalyptech.minecraft.xray.dtf.ByteTag;
import com.apocalyptech.minecraft.xray.dtf.IntTag;

public class PaintingEntity {

    public float tile_x;

    public float tile_y;

    public float tile_z;

    public String name;

    public byte dir;

    public PaintingEntity(CompoundTag t) {
        IntTag tile_x = (IntTag) t.getTagWithName("TileX");
        IntTag tile_y = (IntTag) t.getTagWithName("TileY");
        IntTag tile_z = (IntTag) t.getTagWithName("TileZ");
        StringTag name = (StringTag) t.getTagWithName("Motive");
        ByteTag dir = (ByteTag) t.getTagWithName("Dir");
        this.tile_x = tile_x.value;
        this.tile_y = tile_y.value;
        this.tile_z = tile_z.value;
        this.name = name.value;
        this.dir = dir.value;
    }
}
